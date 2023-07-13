import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class User {
    private String username;
    private String password;
    private String phoneNumber;
    private ShoppingCart shoppingCart;

    public User(String username, String password, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.shoppingCart = new ShoppingCart();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}


class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class ShoppingCart {
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public ShoppingCart() {
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
        System.out.println("商品已添加到购物车。");
    }

    public void removeProduct(Product product) {
        if (products.remove(product)) {
            System.out.println("商品已从购物车移除。");
        } else {
            System.out.println("购物车中不存在该商品。");
        }
    }

    public void modifyProduct(Product product, double newPrice) {
        for (Product p : products) {
            if (p.getName().equals(product.getName())) {
                p.setPrice(newPrice);
                System.out.println("商品价格已修改。");
                return;
            }
        }
        System.out.println("购物车中不存在该商品。");
    }

    public void checkout() {
        double total = 0.0;
        System.out.println("购物车中的商品：");
        for (Product product : products) {
            System.out.println(product.getName() + " - ￥" + product.getPrice());
            total += product.getPrice();
        }
        System.out.println("总计金额：￥" + total);
        System.out.println("结账成功！");
        // 清空购物车
        products.clear();
    }

    public void viewShoppingHistory(String username) {
        System.out.println("购物历史记录：");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("shopping_history.txt"));
            String line;
            boolean displayRecord = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("购买人：" + username)) {
                    displayRecord = true;
                } else if (line.equals("")) {
                    displayRecord = false;
                }
                if (displayRecord) {
                    System.out.println(line);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("无法读取购物历史记录。");
        }
    }


    public double getTotalAmount() {
        double total = 0.0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }
}

class UserManager {
    private List<User> users;
    private User currentUser;

    public UserManager() {
        users = new ArrayList<>();
        currentUser = null;
    }

    public void registerUser(String username, String password, String phoneNumber) {
        User user = new User(username, password, phoneNumber);
        users.add(user);
        System.out.println("注册成功！");
        saveUsersToFile();
    }

    public void loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("登录成功！");
                return;
            }
        }
        System.out.println("用户名或密码错误！");
    }

    public User getCurrentUser() {
        return currentUser;
    }


    public void logoutUser() {
        currentUser = null;
        System.out.println("已退出登录。");
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void changePassword(String currentPassword, String newPassword) {
        if (currentUser != null && currentUser.getPassword().equals(currentPassword)) {
            currentUser.setPassword(newPassword);
            System.out.println("密码修改成功！");
            saveUsersToFile();
        } else {
            System.out.println("当前密码不正确！");
        }
    }

    public void resetPassword(String userName) {
        for (User user : users) {
            if (user.getUsername().equals(userName)) {
                String phoneNumber = user.getPhoneNumber();
                String newPass = phoneNumber.substring(phoneNumber.length() - 6);
                user.setPassword(newPass);
                System.out.println("密码重置成功！新密码为：" + newPass);
                saveUsersToFile();
                return;
            }
        }
        System.out.println("用户名不正确！");
    }

    void saveUsersToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"));
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getPhoneNumber());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("无法保存用户信息到文件。");
        }
    }

    private void loadUsersFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3) {
                    String username = userData[0];
                    String password = userData[1];
                    String phoneNumber = userData[2];
                    User user = new User(username, password, phoneNumber);
                    users.add(user);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("无法读取用户信息文件。");
        }
    }

    public void initialize() {
        loadUsersFromFile();
    }

    public List<User> getUsers() {
        return users;
    }
}

class ProductManager {
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public ProductManager() {
        products = new ArrayList<>();
    }

    public void addProduct(String name, double price) {
        Product product = new Product(name, price);
        products.add(product);
        System.out.println("商品添加成功！");
        saveProductsToFile();
    }

    public void removeProduct(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                products.remove(product);
                System.out.println("商品已移除。");
                saveProductsToFile();
                return;
            }
        }
        System.out.println("商品不存在。");
    }

    public void modifyProduct(String name, double newPrice) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                product.setPrice(newPrice);
                System.out.println("商品价格已修改。");
                saveProductsToFile();
                return;
            }
        }
        System.out.println("商品不存在。");
    }

    public void viewAllProducts() {
        System.out.println("所有商品信息：");
        for (Product product : products) {
            System.out.println("商品名：" + product.getName() + "，价格：￥" + product.getPrice());
        }
    }

    public void searchProduct(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                System.out.println("商品信息：");
                System.out.println("商品名：" + product.getName() + "，价格：￥" + product.getPrice());
                return;
            }
        }
        System.out.println("商品不存在。");
    }

    private void saveProductsToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"));
            for (Product product : products) {
                writer.write(product.getName() + "," + product.getPrice());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("无法保存商品信息到文件。");
        }
    }

    private void loadProductsFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("products.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] productData = line.split(",");
                if (productData.length == 2) {
                    String name = productData[0];
                    double price = Double.parseDouble(productData[1]);
                    Product product = new Product(name, price);
                    products.add(product);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("无法读取商品信息文件。");
        }
    }

    public void initialize() {
        loadProductsFromFile();
    }
}

public class ShoppingSystem {
    private static UserManager userManager;
    private static ProductManager productManager;
    private static ShoppingCart shoppingCart;

    private static User currentUser;


    private static String adminPassword = "manager171";

    public static String getAdminPassword() {
        return adminPassword;
    }

    public static User getCurrentUser() {
        return userManager.getCurrentUser();
    }

    public static void setAdminPassword(String adminPassword) {
        ShoppingSystem.adminPassword = adminPassword;
    }

    public static void main(String[] args) {
        userManager = new UserManager();
        productManager = new ProductManager();
        shoppingCart = new ShoppingCart();

        Scanner scanner = new Scanner(System.in);

        userManager.initialize();
        productManager.initialize();

        while (true) {
            System.out.println("请选择用户界面或管理员界面：");
            System.out.println("1. 用户界面");
            System.out.println("2. 管理员界面");
            System.out.println("3. 退出系统");
            System.out.print("请输入选择的序号：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 读取换行符

            switch (choice) {
                case 1:
                    userInterface(scanner);
                    break;
                case 2:
                    adminInterface(scanner);
                    break;
                case 3:
                    System.out.println("感谢使用！");
                    return;
                default:
                    System.out.println("输入有误，请重新选择。");
            }
        }
    }

    private static void userInterface(Scanner scanner) {
        while (true) {
            System.out.println("-------- 用户界面 --------");
            System.out.println("1. 登录");
            System.out.println("2. 注册");
            System.out.println("3. 返回");
            System.out.print("请输入选择的序号：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 读取换行符

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    register(scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("输入有误，请重新选择。");
            }
        }
    }

    private static void adminInterface(Scanner scanner) {
        while (true) {
            System.out.println("-------- 管理员界面 --------");
            System.out.println("1. 登录");
            System.out.println("2. 返回");
            System.out.print("请输入选择的序号：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 读取换行符

            switch (choice) {
                case 1:
                    adminLogin(scanner);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("输入有误，请重新选择。");
            }
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("请输入用户名：");
        String username = scanner.nextLine();
        System.out.print("请输入密码：");
        String password = scanner.nextLine();
        userManager.loginUser(username, password);

        if (userManager.isLoggedIn()) {
            userLoggedInInterface(scanner);
        }
    }

    private static void register(Scanner scanner) {
        System.out.print("请输入用户名：");
        String username = scanner.nextLine();
        System.out.print("请输入密码：");
        String password = scanner.nextLine();
        System.out.print("请输入电话号码：");
        String phoneNumber = scanner.nextLine();
        userManager.registerUser(username, password, phoneNumber);
    }

    private static void userLoggedInInterface(Scanner scanner) {
        User currentUser = userManager.getCurrentUser();
        if (currentUser == null) {
            System.out.println("用户未登录！");
            return;
        }

        while (true) {
            System.out.println("-------- 用户界面 --------");
            System.out.println("1. 密码管理");
            System.out.println("2. 购物");
            System.out.println("3. 退出登录");
            System.out.print("请输入选择的序号：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 读取换行符

            switch (choice) {
                case 1:
                    passwordManagement(scanner);
                    break;
                case 2:
                    shopping(scanner, currentUser);
                    break;
                case 3:
                    userManager.logoutUser();
                    return;
                default:
                    System.out.println("输入有误，请重新选择。");
            }
        }
    }

    private static void passwordManagement(Scanner scanner) {
        while (true) {
            System.out.println("-------- 密码管理 --------");
            System.out.println("1. 修改密码");
            System.out.println("2. 返回");
            System.out.print("请输入选择的序号：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 读取换行符

            switch (choice) {
                case 1:
                    changePassword(scanner);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("输入有误，请重新选择。");
            }
        }
    }

    private static void changePassword(Scanner scanner) {
        System.out.print("请输入当前密码：");
        String currentPassword = scanner.nextLine();
        System.out.print("请输入新密码：");
        String newPassword = scanner.nextLine();
        userManager.changePassword(currentPassword, newPassword);
    }


    private static void shopping(Scanner scanner, User user) {
        ShoppingCart shoppingCart = user.getShoppingCart();

        while (true) {
            System.out.println("-------- 购物 --------");
            System.out.println("1. 将商品加入购物车");
            System.out.println("2. 从购物车中移除商品");
            System.out.println("3. 修改购物车中的商品");
            System.out.println("4. 模拟结账");
            System.out.println("5. 查看购物历史记录");
            System.out.println("6. 返回");
            System.out.print("请输入选择的序号：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 读取换行符

            switch (choice) {
                case 1:
                    addToCart(scanner, shoppingCart);
                    break;
                case 2:
                    removeFromCart(scanner, shoppingCart);
                    break;
                case 3:
                    modifyCart(scanner, shoppingCart);
                    break;
                case 4:
                    checkout(scanner, shoppingCart);
                    break;
                case 5:
                    shoppingCart.viewShoppingHistory(user.getUsername());
                    break;
                case 6:
                    return;
                default:
                    System.out.println("输入有误，请重新选择。");
            }
        }
    }

    private static void addToCart(Scanner scanner, ShoppingCart shoppingCart) {
        System.out.print("请输入商品名：");
        String name = scanner.nextLine();
        Product product = getProductByName(name);
        if (product != null) {
            shoppingCart.addProduct(product);
        } else {
            System.out.println("商品不存在。");
        }
    }

    private static void removeFromCart(Scanner scanner, ShoppingCart shoppingCart) {
        System.out.print("请输入商品名：");
        String name = scanner.nextLine();
        Product product = getProductByName(name);
        if (product != null) {
            shoppingCart.removeProduct(product);
        } else {
            System.out.println("商品不存在。");
        }
    }

    private static void modifyCart(Scanner scanner, ShoppingCart shoppingCart) {
        System.out.print("请输入商品名：");
        String name = scanner.nextLine();
        Product product = getProductByName(name);
        if (product != null) {
            System.out.print("请输入新的价格：");
            double newPrice = scanner.nextDouble();
            scanner.nextLine(); // 读取换行符
            shoppingCart.modifyProduct(product, newPrice);
        } else {
            System.out.println("商品不存在。");
        }
    }

    private static void checkout(Scanner scanner, ShoppingCart shoppingCart) {
        if (shoppingCart.getProducts().isEmpty()) {
            System.out.println("购物车为空，无法结账。");
        } else {
            saveShoppingHistory(shoppingCart, userManager.getCurrentUser());
            shoppingCart.checkout();
        }
    }

    private static Product getProductByName(String name) {
        for (Product product : productManager.getProducts()) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    private static void saveShoppingHistory(ShoppingCart shoppingCart, User user) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("shopping_history.txt", true));
            writer.write("购买人：" + user.getUsername());
            writer.newLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String purchaseTime = dateFormat.format(new Date());

            writer.write("购买时间：" + purchaseTime);
            writer.newLine();
            for (Product product : shoppingCart.getProducts()) {
                writer.write("商品名：" + product.getName() + "，价格：￥" + product.getPrice());
                writer.newLine();
            }
            writer.write("总计金额：￥" + shoppingCart.getTotalAmount());
            writer.newLine();
            writer.newLine(); // 添加空行，用于分隔不同用户的购物记录
            writer.close();
        } catch (IOException e) {
            System.out.println("无法保存购物历史记录到文件。");
        }
    }


    private static void adminLogin(Scanner scanner) {
        System.out.print("请输入管理员密码：");
        String password = scanner.nextLine();
        if (password.equals(adminPassword)) {
            System.out.println("登录成功！");
            adminLoggedInInterface(scanner);
        } else {
            System.out.println("密码错误！");
        }
    }

    private static void adminLoggedInInterface(Scanner scanner) {
        while (true) {
            System.out.println("-------- 管理员界面 --------");
            System.out.println("1. 密码管理");
            System.out.println("2. 商品管理");
            System.out.println("3. 用户管理");
            System.out.println("4. 退出登录");
            System.out.print("请输入选择的序号：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 读取换行符

            switch (choice) {
                case 1:
                    adminPasswordManagement(scanner);
                    break;
                case 2:
                    productManagement(scanner);
                    break;
                case 3:
                    userManagement(scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("输入有误，请重新选择。");
            }
        }
    }

    private static void adminPasswordManagement(Scanner scanner) {
        while (true) {
            System.out.println("-------- 密码管理 --------");
            System.out.println("1. 修改管理员密码");
            System.out.println("2. 重置用户密码");
            System.out.println("3. 返回");
            System.out.print("请输入选择的序号：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 读取换行符

            switch (choice) {
                case 1:
                    changeAdminPassword(scanner);
                    break;
                case 2:
                    resetPassword(scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("输入有误，请重新选择。");
            }
        }
    }

    private static void changeAdminPassword(Scanner scanner) {
        System.out.print("请输入当前管理员密码：");
        String currentPassword = scanner.nextLine();
        System.out.print("请输入新密码：");
        String newPassword = scanner.nextLine();
        setAdminPassword(newPassword);
    }

    private static void resetPassword(Scanner scanner) {
        System.out.print("请输入用户名：");
        String userName = scanner.nextLine();
        userManager.resetPassword(userName);
    }

    private static void productManagement(Scanner scanner) {
        while (true) {
            System.out.println("-------- 商品管理 --------");
            System.out.println("1. 添加商品");
            System.out.println("2. 移除商品");
            System.out.println("3. 修改商品价格");
            System.out.println("4. 查看所有商品");
            System.out.println("5. 搜索商品");
            System.out.println("6. 返回");
            System.out.print("请输入选择的序号：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 读取换行符

            switch (choice) {
                case 1:
                    addProduct(scanner);
                    break;
                case 2:
                    removeProduct(scanner);
                    break;
                case 3:
                    modifyProduct(scanner);
                    break;
                case 4:
                    productManager.viewAllProducts();
                    break;
                case 5:
                    searchProduct(scanner);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("输入有误，请重新选择。");
            }
        }
    }

    private static void addProduct(Scanner scanner) {
        System.out.print("请输入商品名：");
        String name = scanner.nextLine();
        System.out.print("请输入价格：");
        double price = scanner.nextDouble();
        scanner.nextLine(); // 读取换行符
        productManager.addProduct(name, price);
    }

    private static void removeProduct(Scanner scanner) {
        System.out.print("请输入商品名：");
        String name = scanner.nextLine();
        productManager.removeProduct(name);
    }

    private static void modifyProduct(Scanner scanner) {
        System.out.print("请输入商品名：");
        String name = scanner.nextLine();
        System.out.print("请输入新的价格：");
        double newPrice = scanner.nextDouble();
        scanner.nextLine(); // 读取换行符
        productManager.modifyProduct(name, newPrice);
    }

    private static void searchProduct(Scanner scanner) {
        System.out.print("请输入商品名：");
        String name = scanner.nextLine();
        productManager.searchProduct(name);
    }

    private static void userManagement(Scanner scanner) {
        while (true) {
            System.out.println("-------- 用户管理 --------");
            System.out.println("1. 列出所有用户信息");
            System.out.println("2. 删除用户信息");
            System.out.println("3. 查询用户信息");
            System.out.println("4. 返回");
            System.out.print("请输入选择的序号：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 读取换行符

            switch (choice) {
                case 1:
                    listUsers();
                    break;
                case 2:
                    removeUser(scanner);
                    break;
                case 3:
                    searchUser(scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("输入有误，请重新选择。");
            }
        }
    }

    private static void listUsers() {
        List<User> users = userManager.getUsers();
        if (users.isEmpty()) {
            System.out.println("没有用户信息。");
        } else {
            System.out.println("所有用户信息：");
            for (User user : users) {
                System.out.println("用户名：" + user.getUsername() + "，电话号码：" + user.getPhoneNumber());
            }
        }
    }

    private static void removeUser(Scanner scanner) {
        System.out.print("请输入要删除的用户名：");
        String username = scanner.nextLine();
        for (User user : userManager.getUsers()) {
            if (user.getUsername().equals(username)) {
                userManager.getUsers().remove(user);
                System.out.println("用户信息已删除。");
                userManager.saveUsersToFile();
                return;
            }
        }
        System.out.println("用户不存在。");
    }

    private static void searchUser(Scanner scanner) {
        System.out.print("请输入要查询的用户名：");
        String username = scanner.nextLine();
        for (User user : userManager.getUsers()) {
            if (user.getUsername().equals(username)) {
                System.out.println("用户信息：");
                System.out.println("用户名：" + user.getUsername() + "，电话号码：" + user.getPhoneNumber());
                return;
            }
        }
        System.out.println("用户不存在。");
    }
}
