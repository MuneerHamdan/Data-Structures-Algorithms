    package restaurant;

    import java.util.ArrayList;

    public class RUHungry {
        
        // Menu: two parallel arrays. The index in one corresponds to the same index in the other.
        private String[] categoryVar; // array where containing the name of menu categories (e.g. Appetizer, Dessert).
        private MenuNode[] menuVar;   // array of lists of MenuNodes where each index is a category.
        
        // Stock: hashtable using chaining to resolve collisions.
        private StockNode[] stockVar;  // array of linked lists of StockNodes (use hashfunction to organize Nodes: id % stockVarSize)
        private int stockVarSize;

        // Transactions: orders, donations, restock transactions are recorded 
        private TransactionNode transactionVar; // refers to the first front node in linked list

        // Queue keeps track of people who've left the restaurant
        private Queue<People> leftQueueVar;  

        // Tables
        private People[] tables;        // array for people who are currently sitting
        private int[][]  tablesInfo;    // 2-D integer array where the first row contains how many seats there are at each table (each index)
                                        // and the second row contains "0" or "1", where 1 is the table is not available and 0 the opposite


        public RUHungry () {
            categoryVar    = null;
            menuVar        = null;
            stockVar       = null;
            stockVarSize   = 0;
            transactionVar = null;
            leftQueueVar   = null;
            tablesInfo     = null;
            tables         = null;
        }

        public MenuNode[] getMenu() { return menuVar; }
        public String[] getCategoryArray() { return categoryVar;}
        public StockNode[] getStockVar() { return stockVar; } 
        public TransactionNode getFrontTransactionNode() { return transactionVar; } 
        public TransactionNode resetFrontNode() {return transactionVar = null;} // method to reset the transactions for a new day
        public Queue<People> getLeftQueueVar() { return leftQueueVar; } 
        public int[][] getTablesInfo() { return tablesInfo; }



        public void menu(String inputFile) {

            StdIn.setFile(inputFile);

            int categories = StdIn.readInt();
            categoryVar = new String[categories];
            menuVar = new MenuNode[categories];
            ArrayList<MenuNode> menus = new ArrayList<MenuNode>();

            
            for (int i = 0; i < categories; i++){

                String name = StdIn.readString();
                int dishcount = StdIn.readInt();
                String dishname = null;
                StdIn.readLine();
                for (int j = 0; j < dishcount; j++){
                    dishname = StdIn.readLine();
                    int ingredientcount = StdIn.readInt();
                    int[] ingredients = new int[ingredientcount];
                    for (int z = 0; z < ingredientcount; z++){
                        ingredients[z] = StdIn.readInt();
                    }
                    StdIn.readLine();
                    Dish dish = new Dish(name, dishname, ingredients);
                    MenuNode menu = new MenuNode(dish, null);
                    menus.add(menu);
                }
                categoryVar[i] = name;
                MenuNode pointer = menus.get(menus.size()-1);
                menuVar[i] = pointer;
                for (int j = 0; j < dishcount-1; j++){
                    while (pointer.getNextMenuNode() != null){
                        pointer = pointer.getNextMenuNode();
                    }
                    pointer.setNextMenuNode(menus.get(menus.size()-2));
                    menus.remove(menus.size()-2);
                }
                menus.remove(0);
            }


        }

        public MenuNode findDish ( String dishName ) {

            MenuNode menuNode = null;

            // Search all categories since we don't know which category dishName is at
            for ( int category = 0; category < menuVar.length; category++ ) {

                MenuNode ptr = menuVar[category]; // set ptr at the front (first menuNode)
                
                while ( ptr != null ) { // while loop that searches the LL of the category to find the itemOrdered
                    if ( ptr.getDish().getDishName().equals(dishName) ) {
                        return ptr;
                    } else{
                        ptr = ptr.getNextMenuNode();
                    }
                }
            }
            return menuNode;
        }


        public int findCategoryIndex ( String category ) {
            int index = 0;
            for ( int i = 0; i < categoryVar.length; i++ ){
                if ( category.equalsIgnoreCase(categoryVar[i]) ) {
                    index = i;
                    break;
                }
            }
            return index;
        }


        public void addStockNode ( StockNode newNode ) {

            // WRITE YOUR CODE HERE

            int id = newNode.getIngredient().getID();

            int index = id % stockVarSize;

            if (stockVar[index] != null){

                newNode.setNextStockNode(stockVar[index]);
                stockVar[index] = newNode;

            }
            else {
                stockVar[index] = newNode;
            }

        }


        public void deleteStockNode ( String ingredientName ) {


            StockNode ptr = stockVar[0];
            StockNode child = stockVar[0].getNextStockNode();

            for (int i = 0; i < stockVarSize; i++){
                while (child != null){

                    if (ptr.getIngredient().getName().equalsIgnoreCase(ingredientName)){
                        stockVar[i] = child;
                        return;
                    }
                    if (child.getIngredient().getName().equalsIgnoreCase(ingredientName)){
                        if (child.getNextStockNode() == null){
                            ptr.setNextStockNode(null);
                            return;
                        }
                        else {
                            ptr.setNextStockNode(child.getNextStockNode());
                            return;
                        }
                    }
                    
                    ptr = ptr.getNextStockNode();
                    child = ptr.getNextStockNode();
                }
                ptr = stockVar[i+1];
                child = ptr.getNextStockNode();
            }
        }

       
        public StockNode findStockNode (int ingredientID) {

            
            int index = ingredientID % stockVarSize;

            StockNode ptr = stockVar[index];
            while (ptr != null){
                if (ptr.getIngredient().getID() == ingredientID) return ptr;
                ptr = ptr.getNextStockNode();
            }


            return null; // update the return value
        }

        public StockNode findStockNode (String ingredientName) {
            
            StockNode stockNode = null;
            
            for ( int index = 0; index < stockVar.length; index ++ ){
                
                StockNode ptr = stockVar[index];
                
                while ( ptr != null ){
                    if ( ptr.getIngredient().getName().equalsIgnoreCase(ingredientName) ){
                        return ptr;
                    } else {  
                        ptr = ptr.getNextStockNode();
                    }
                }
            }
            return stockNode;
        }
        
        public void updateStock (String ingredientName, int ingredientID, int stockAmountToAdd) {
            

            StockNode ptr;
            if (ingredientName != null){
                ptr = findStockNode(ingredientName);
                ptr.getIngredient().setStockLevel(ptr.getIngredient().getStockLevel() + stockAmountToAdd);
                return;
            }
            if (ingredientID != -1){
                ptr = findStockNode(ingredientID);
                ptr.getIngredient().setStockLevel(ptr.getIngredient().getStockLevel() + stockAmountToAdd);
                return;
            }
        }

        public void updatePriceAndProfit() {

            MenuNode ptr;

            for (int i = 0; i < menuVar.length; i++){
                ptr = menuVar[i];
                while (ptr != null){
                    double cost = 0;
                    for (int j = 0; j < ptr.getDish().getStockID().length; j++){
                        StockNode ptr2 = findStockNode(ptr.getDish().getStockID()[j]);
                        cost = cost + ptr2.getIngredient().getCost();
                    }
                    double price = cost * 1.2;
                    ptr.getDish().setPriceOfDish(price);
                    double profit = price - cost;
                    ptr.getDish().setProfit(profit);
                    ptr = ptr.getNextMenuNode();
                }

            }
        }

        public void createStockHashTable (String inputFile){
            
            StdIn.setFile(inputFile);
            
            
            stockVarSize = StdIn.readInt();
            stockVar = new StockNode[stockVarSize];
            

            while (!StdIn.isEmpty()){

                int ingredientID = StdIn.readInt();
                StdIn.readChar();
                String name = StdIn.readLine();
                double price = StdIn.readDouble();
                StdIn.readChar();
                int amount = StdIn.readInt();

                Ingredient ingredient = new Ingredient(ingredientID, name, amount, price);
                StockNode stockNode = new StockNode(ingredient, null);
                addStockNode(stockNode);
            }
        }

        public void addTransactionNode ( TransactionData data ) { // method adds new transactionNode to the end of LL
           

            TransactionNode transactionNode = new TransactionNode(data, null);
            if (transactionVar == null){
                transactionVar = new TransactionNode(data, null);
                return;
            }
            TransactionNode ptr = transactionVar;
            while (ptr.getNext() != null){
                ptr = ptr.getNext();
            }
            ptr.setNext(transactionNode);
        }

        public boolean checkDishAvailability ( String dishName, int numberOfDishes ){
            

            MenuNode dishptr = findDish(dishName);
            StockNode stockptr;
            for (int i = 0; i < dishptr.getDish().getStockID().length; i++){
                stockptr = findStockNode(dishptr.getDish().getStockID()[i]);
                if (stockptr.getIngredient().getStockLevel() < numberOfDishes) return false;
            }
            
            return true; // update the return value
        }

        public void order ( String dishName, int quantity ){


            MenuNode ptr = findDish(dishName);

            if (checkDishAvailability(dishName, quantity) == true){
                TransactionData transactionData = new TransactionData("order", dishName, quantity, findDish(dishName).getDish().getProfit() * quantity, true);
                addTransactionNode(transactionData);

                for (int i = 0; i < ptr.getDish().getStockID().length; i++){
                    StockNode stockptr = findStockNode(ptr.getDish().getStockID()[i]);
                    updateStock(stockptr.getIngredient().getName(), stockptr.getIngredient().getID(), (-1 * quantity));
                }
                return;
            }
            else if (checkDishAvailability(dishName, quantity) == false){
                MenuNode dishptr = findDish(dishName);
                TransactionData transactionData = new TransactionData("order", dishName, quantity, 0, false);
                addTransactionNode(transactionData);

                // case 1: normal
                if (dishptr.getNextMenuNode() != null){
                    dishptr = dishptr.getNextMenuNode();
                    while (dishptr != null){
                        if (checkDishAvailability(dishptr.getDish().getDishName(), quantity)){
                            TransactionData transactionData2 = new TransactionData("order", dishptr.getDish().getDishName(), quantity, findDish(dishptr.getDish().getDishName()).getDish().getProfit() * quantity, true);
                            addTransactionNode(transactionData2);

                            for (int i = 0; i < dishptr.getDish().getStockID().length; i++){
                                StockNode stockptr = findStockNode(dishptr.getDish().getStockID()[i]);
                                updateStock(stockptr.getIngredient().getName(), stockptr.getIngredient().getID(), (-1 * quantity));
                            }
                            return;
                        }
                        else {
                            TransactionData transactionData3 = new TransactionData("order", dishptr.getDish().getDishName(), quantity, 0, false);
                            addTransactionNode(transactionData3);
                        }
                        dishptr = dishptr.getNextMenuNode();
                    }
                    dishptr = findDish(dishName);
                    dishptr = menuVar[findCategoryIndex(dishptr.getDish().getCategory())];
                    while (!dishptr.getDish().getDishName().equalsIgnoreCase(dishName)){
                        if (checkDishAvailability(dishptr.getDish().getDishName(), quantity) == true){
                            TransactionData transactionData2 = new TransactionData("order", dishptr.getDish().getDishName(), quantity, findDish(dishptr.getDish().getDishName()).getDish().getProfit() * quantity, true);
                            addTransactionNode(transactionData2);

                            for (int i = 0; i < dishptr.getDish().getStockID().length; i++){
                                StockNode stockptr = findStockNode(dishptr.getDish().getStockID()[i]);
                                updateStock(stockptr.getIngredient().getName(), stockptr.getIngredient().getID(), (-1 * quantity));
                            }
                            return;
                        }
                        else {
                            TransactionData transactionData3 = new TransactionData("order", dishptr.getDish().getDishName(), quantity, 0, false);
                            addTransactionNode(transactionData3);
                        }
                        dishptr = dishptr.getNextMenuNode();
                    }
                    return;
                }

                //case 2: starts at end
                if (dishptr.getNextMenuNode() == null){
                    dishptr = menuVar[findCategoryIndex(dishptr.getDish().getCategory())];
                    while (!dishptr.getDish().getDishName().equals(dishName)){
                        if (checkDishAvailability(dishptr.getDish().getDishName(), quantity)){
                            TransactionData transactionData2 = new TransactionData("order", dishptr.getDish().getDishName(), quantity, dishptr.getDish().getProfit() * quantity, true);
                            addTransactionNode(transactionData2);

                            for (int i = 0; i < dishptr.getDish().getStockID().length; i++){
                                StockNode stockptr = findStockNode(dishptr.getDish().getStockID()[i]);
                                updateStock(stockptr.getIngredient().getName(), stockptr.getIngredient().getID(), (-1 * quantity));
                            }
                            return;
                        }
                        else {
                            TransactionData transactionData3 = new TransactionData("order", dishptr.getDish().getDishName(), quantity, 0, false);
                            addTransactionNode(transactionData3);
                        }
                        dishptr = dishptr.getNextMenuNode();
                    }

                    return;
                }


                
            }
        }

        public double profit () {

            
            double p = 0;
            TransactionNode ptr = transactionVar;
            while (ptr != null){
                p = p + ptr.getData().getProfit();
                ptr = ptr.getNext();
            }

            return p; 
        }

        public void donation ( String ingredientName, int quantity ){


            StockNode s = findStockNode(ingredientName);
            if ((profit() > 50) && (s.getIngredient().getStockLevel() >= quantity)){
                TransactionData t = new TransactionData("donation", ingredientName, quantity, 0, true);
                addTransactionNode(t);
                updateStock(ingredientName, s.getIngredient().getID(), (-1 * quantity));
            }
            else {
                TransactionData t = new TransactionData("donation", ingredientName, quantity, 0, false);
                addTransactionNode(t);
            }
        }

        public void restock ( String ingredientName, int quantity ){


            StockNode stockptr = findStockNode(ingredientName);
            double profit = quantity * stockptr.getIngredient().getCost();
            
            double z = profit();
            if (z > profit){
                TransactionData transactionData = new TransactionData("restock", ingredientName, quantity, (-1 * profit), true);
                addTransactionNode(transactionData);

                updateStock(ingredientName, stockptr.getIngredient().getID(), quantity);
            }
            else {
                TransactionData transactionData = new TransactionData("restock", ingredientName, quantity, 0, false);
                addTransactionNode(transactionData);
            }
        }

        public void createTables ( String inputFile ) { 

            StdIn.setFile(inputFile);
            int numberOfTables = StdIn.readInt();
            tablesInfo = new int[2][numberOfTables];
            tables = new People[numberOfTables];
            
            for ( int t = 0; t < numberOfTables; t++ ) {
                tablesInfo[0][t] = StdIn.readInt() * StdIn.readInt();
            }
        }

