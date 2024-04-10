import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int users;
        int totalPrice;
        int pricePerUser;
        ArrayList<Item> itemList;

        System.out.println("Привет! Давай поделим чек!");
        users = requestUsers();
        System.out.println("Теперь давай заполним товары!");
        itemList = newRequestItems();
        System.out.println("А теперь посчитаем!");
        System.out.println("Список товаров:");
        displayItems(itemList);
        System.out.println("______________________");
        totalPrice = calculateTotalPrice(itemList);
        System.out.println("Итоговая цена: " + priceIntToCustomString(totalPrice) + rubleWording(totalPrice));
        pricePerUser = totalPrice / users;
        System.out.println("C человека: " + priceIntToCustomString(pricePerUser) + rubleWording(pricePerUser));
    }

    //Метод до победного запрашивающий у пользователя кол-во человек
    private static int requestUsers() {
        int users;
        Scanner scanner = new Scanner(System.in);

        System.out.println("На скольких мы разделим чек? Минимум 2.");

        while (true) {
            //проверяем что в сканнере int
            if (scanner.hasNextInt()) {
                users = scanner.nextInt();
                //проверяем что в int подходящее значение
                if (users < 1) System.out.println("Некорректный ввод. Минимум 2.");
                else if (users == 1) System.out.println("Некорректный ввод. На 1 ты и сам можешь поделить. Минимум 2.");
                else if (users > 1) {
                    System.out.println("Принято! Колличество человек: " + users);
                    return users;
                }
            } else {
                System.out.println("Некорректный ввод. Введи целое число.");
            }
            scanner.nextLine();
        }
    }

    //Метод заполняющий массив товарами до ввода "Завершить"
    /*
    private static ArrayList<Item> requestItems() {
        ArrayList<Item> itemList = new ArrayList<>();
        String input = "";
        String name = "";
        int price = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Вводи каждый товар по порядку в формате: '[Название] [Цена]'");
        System.out.println("Где [Название] это строка, а [Цена] это число в формате рубли.копейки, например 9.00");
        System.out.println("Чтобы закончить ввод напиши 'Завершить'");

        while (true) {
            input = scanner.nextLine();
            char[] inputChar = input.toCharArray();
            int len = inputChar.length; //Переменная с длинной массива символов, чтобы код читабильнее был

            //С конца массива проверяем что 1, 2 и 4 символы это цифры, и что 3 символ это точка
            //Довольно всрато, но лучше я не придумал и сроки уже поджимают
            //Наверное лучше было бы проверять с помощью regex, но я их ещё не умею писать
            if ((len >= 5) && Character.isDigit(inputChar[len - 1]) && Character.isDigit(inputChar[len - 2]) && (inputChar[len - 3] == '.') && Character.isDigit(inputChar[len - 4])) {
                String priceString = "";
                //парсим массив символов с конца, формироуем строку с ценой товара
                for (int i = len - 1; i > -1; i--) {
                    if (Character.isDigit(inputChar[i])) {
                        priceString = inputChar[i] + priceString;
                    } else if(inputChar[i] == ' ') {
                        //после нахождения проблема разделяющего цену от названия, присваиваем обе переменные и выходим из цикла
                        price = Integer.parseInt(priceString);
                        name = String.valueOf(inputChar).substring(0, i);
                        break;
                    }
                }
                System.out.println("Товар успешно добавлен: " + input);
                itemList.add(new Item(name, price));
            } else if (input.equalsIgnoreCase("завершить")) {
                System.out.println("Заполнение списка товаров закончено.");
                break;
            } else System.out.println("Некорректный ввод. Используй корректное форматирование.");
        }
        return itemList;
    }
    */

    //Переписанный метод заполнения массива товаров
    private static ArrayList<Item> newRequestItems() {
        ArrayList<Item> itemList = new ArrayList<>();
        String input = "";
        String name = "";
        String[] inputStringArray;
        int price = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Вводи каждый товар по порядку в формате: '[Название] [Цена]'");
        System.out.println("Где [Название] это строка, а [Цена] это положительное число в формате рубли.копейки, например 9.00");
        System.out.println("Чтобы закончить ввод напиши 'Завершить'");

        while (true) {
            input = scanner.nextLine().trim();
            inputStringArray = input.split(" ");
            int len = inputStringArray.length;
            String priceString = inputStringArray[len - 1];

            //научился писать regex, это оказалось не так уж сложно :)
            if (len > 1 && (priceString.matches("^\\d*\\d[.]\\d\\d$"))) {
                //если больше одной строки и последняя строка соответсвует формату, то пропускаем
                priceString = priceString.replaceAll("[.]", "");
                price = Integer.parseInt(priceString);
                name = input.substring(0, input.lastIndexOf(" "));
                itemList.add(new Item(name, price));
                System.out.println("Товар успешно добавлен: " + input + rubleWording(price));
            } else if (input.equalsIgnoreCase("Завершить")) {
                System.out.println("Заполнение списка товаров закончено.");
                break;
            } else {
                System.out.println("Некорректный ввод. Используй корректное форматирование.");
            }
        }

        return itemList;
    }

    //Преобразуем int в строку в нужном формате
    private static String priceIntToCustomString(int price) {
        String output = String.valueOf(price);
        //Если строка не достаточно длинная, то исправляем это, релеватно для цены типа 0.01
        while (true) {
            if (output.length() < 3) output = "0" + output;
            else break;
        }
        output = new StringBuilder(output).insert(output.length()-2, ".").toString();
        return output;
    }

    //Выводит в консоль все товар в списке
    private static void displayItems(ArrayList<Item> itemList) {
        for (Item item : itemList) {
            System.out.println(item.name + " " + priceIntToCustomString(item.price) + rubleWording(item.price));
        }
    }

    //Считает общую стоимость всех товаров в списке
    private static int calculateTotalPrice(ArrayList<Item> itemList) {
        int output = 0;
        for (Item item : itemList) {
            output += item.price;
        }
        return output;
    }

    //Выясняем как нам написать слово 'рубль'
    private static String rubleWording(int price) {
        String output = "";
        String priceString = String.valueOf(price);
        //Если строка не достаточно длинная, то исправляем это, релеватно для цены типа 0.01
        while (true) {
            if (priceString.length() < 3) priceString = "0" + priceString;
            else break;
        }
        int len = priceString.length();
        //цифра третья с конца (первая после запятой)
        int third = priceString.charAt(len - 3) - '0'; //долго мучился с тем что char цифра нормально не транслируется в int. Нашёл такой воркэраунд но мне это нравится

        if (price < 100 || (price >= 500 && price < 2100)) output = " рублей"; // если целая часть цены 0 или от 5 до 20, то рублей
        else if (third == 1) output = " рубль"; // если третья цифра это 1 и это не 11, то рубль
        else if (third >= 2 && third <= 4) output = " рубля"; //если третья цифра от 2 до 4, и это не от 12 до 14, то рубля
        else output = " рублей"; //остальные

        return output;
    }

}

class Item {
    String name = "";
    int price = 0;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }
}