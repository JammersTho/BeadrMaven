import model.Customer;
import model.Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {

    private View theView;
    private Model theModel;

    public Controller(View theView, Model theModel) {
        this.theModel = theModel;
        this.theView = theView;

        this.theView.addCalculationListener(new Listener());
    }

    class Listener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            int firstNumber, secondNumber = 0;

            try {
                firstNumber = theView.getFirstNumber();
                secondNumber = theView.getSecondNumber();

                theModel.addTwoNumbers(firstNumber, secondNumber);
                theView.setCalcSolution(theModel.getCalcValue());


                theModel.createTable();

                theModel.deleteAll("USERS");
                theModel.deleteAll("PRODUCTS");
                theModel.deleteAll("PAYMENTS");
                theModel.deleteAll("ORDERS");
                theModel.deleteAll("CUSTOMERS");
                theModel.deleteAll("BUNDLES");
                theModel.deleteAll("FIXINGS");
                theModel.deleteAll("ORDR_PROD_LINK");
                theModel.deleteAll("PAYM_ORDR_LINK");
                theModel.deleteAll("CUST_ORDR_LINK");
                theModel.deleteAll("PROD_BNDL_LINK");
                theModel.deleteAll("PROD_FXNG_LINK");

                Customer customer = new Customer(1, "Big Co", "Contact Name", "Address", "WR142RG", "email@mail.com", "01684 573 446", "Source");
                theModel.insertCustomer(customer);

                //theModel.selectUserList();
                theView.setUsers(theModel.getUserList());

            } catch (NumberFormatException e) {
                //theView.displayErrorMessage("Need to enter two numbers");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}
