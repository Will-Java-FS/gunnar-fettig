package com.revature.services;

import com.revature.exception.ClientErrorException;
import com.revature.exception.NotFoundException;
import com.revature.exception.UnAuthorizedException;
import com.revature.models.Account;
import com.revature.models.Grocery;
import com.revature.repositories.GroceryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class GroceryService {
    GroceryRepo groceryRepo;

    @Autowired
    public GroceryService(GroceryRepo groceryRepo)
    {
        this.groceryRepo = groceryRepo;
    }

    public Grocery addGrocery(Grocery grocery) throws ClientErrorException, UnAuthorizedException {
        Account loggedInUser = LoggedInUserService.getInstance().getLoggedInUser();

        if(loggedInUser == null) throw new UnAuthorizedException();
        // New grocery requirements: has name, price greater than $0.00 and quantity greater than 0
        if(grocery.getName() == null) throw new ClientErrorException();
        if(grocery.getPrice().compareTo(new BigDecimal("0.0")) <= 0) throw new ClientErrorException();
        if(grocery.getQuantity() < 1) throw new ClientErrorException();
        // Add item to logged in user
        grocery.setOwner(loggedInUser);
        loggedInUser.getGroceries().add(grocery);
        return groceryRepo.save(grocery);
    }

    public List<Grocery> getAllGroceries() throws UnAuthorizedException{
        Account loggedInUser = LoggedInUserService.getInstance().getLoggedInUser();

        if (loggedInUser == null) throw new UnAuthorizedException();
        // Return all if admin OR
        // Return users items
        if (loggedInUser.is_admin()) return groceryRepo.findAll();
        return loggedInUser.getGroceries();
    }

    public Grocery getGroceryByID(int ID) throws NotFoundException {
        Account loggedInUser = LoggedInUserService.getInstance().getLoggedInUser();
        Optional<Grocery> optionalGrocery = groceryRepo.findById(ID);
        // Must be admin or own item to return it
        if (optionalGrocery.isPresent()
                && (optionalGrocery.get().getOwner().getId() == loggedInUser.getId()
                || loggedInUser.is_admin())) {
            return optionalGrocery.get();
        } else {
            throw new NotFoundException();
        }
    }

    public void updateGrocery(int id, Grocery grocery) throws ClientErrorException, NotFoundException {
        Account loggedInUser = LoggedInUserService.getInstance().getLoggedInUser();

        if(loggedInUser == null) throw new ClientErrorException();

        Optional<Grocery> optionalGrocery = groceryRepo.findById(id);
        if(optionalGrocery.isPresent()) {
            Grocery temp = optionalGrocery.get();
            // Must be admin or own item to update it
            if (temp.getOwner().getId() == loggedInUser.getId() || loggedInUser.is_admin()) {
                // Must meet new grocery requirements
                if(grocery.getName() != null) temp.setName(grocery.getName());
                if(grocery.getPrice() != null) {
                    if (grocery.getPrice().compareTo(new BigDecimal("0.0")) <= 0) throw new ClientErrorException();
                    else temp.setPrice(grocery.getPrice());
                }
                if(grocery.getQuantity() != null) {
                    if(grocery.getQuantity() < 1) throw new ClientErrorException();
                    else temp.setQuantity(grocery.getQuantity());
                }
                for (Grocery gro : loggedInUser.getGroceries()) {
                    if (gro.getId() == id) loggedInUser.getGroceries()
                            .set(loggedInUser.getGroceries().indexOf(gro), temp);
                }
                groceryRepo.save(temp);
            }
        } else {
            throw new NotFoundException();
        }
    }

    public Integer deleteGroceryByID(int ID) {
        Account loggedInUser = LoggedInUserService.getInstance().getLoggedInUser();
        Optional<Grocery> optionalGrocery = groceryRepo.findById(ID);

        if (optionalGrocery.isPresent()) {
            Account owner = optionalGrocery.get().getOwner();
            // Must be admin or own item to delete it
            if (owner.getId() == loggedInUser.getId() || loggedInUser.is_admin()) {
                groceryRepo.deleteById(ID);
                loggedInUser.getGroceries().removeIf(gro -> gro.getId() == ID);
                return 1;
            }
        }
        return null;
    }

}
