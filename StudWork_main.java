package com.test.idea;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class StudWork_main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        //классу scanner присваиваем в качестве аргумента system.in
        Scanner scan = new Scanner(System.in);

        //начальное значение для switch case
        int x = 0;
        String s = "";

        //ввод названия таблицы с клавиатуры
        System.out.println("Введите название таблицы: ");
        String tablename = scan.nextLine();

        //цикл работает до тех пор, пока пользователь не введет 5
        while (!"5".equals(s)) {
            System.out.println();
            System.out.println("1. Вывести все таблицы из текущей БД.");
            System.out.println("2. Создать таблицу в БД.");
            System.out.println("3. Добавить данные в таблицу.");
            System.out.println("4. Сохранить данные в Excel.");
            System.out.println("5. Выйти из программы.");
            s = scan.next();

            //пробуем перевести пользовательский ввод в int
            try {
                x = Integer.parseInt(s);
            }
            //выдаем сообщение об ошибке ввода, и так до тех пор, пока пользователь не введет число
            catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода.");
            }

            //оператор switch для множества развилок
            //эквивалентно оператору if
            switch (x) {

                //если пользователь ввел цифру 1, то...
                case 1 -> {
                    sql.TablesOutput();
                }

                //если пользователь ввел цифру 2, то...
                case 2 -> {
                    String query = " (name varchar(100), age int, salary int)";
                    sql.CreatingSQLTable(tablename, query);
                }

                //если пользователь ввел цифу 3, то...
                case 3 -> {

                    //регистрируем драйвер для дальнейшей работы (управление jdbc)
                    //КЛАСС DriverManager, МЕТОД registerDriver
                    DriverManager.registerDriver(new Driver());

                    //имя драйвера
                    //КЛАСС Class, МЕТОД forName
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    //пытаемся установить соединение с заданным url бд
                    //ОБЪЕКТ Connection для работы с бд
                    //КЛАСС DriverManager, МЕТОД getConnection
                    Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "кщще");
                    System.out.println("Успешно законнектились к БД!");

                    //МЕТОД nextLine() ОБЪЕКТА Scanner читает всю текущую строки и возвращает всё, что было в этой строке
                    scan.nextLine();

                    List<Worker> workers = new ArrayList<>();

                    //вводим с клавиатуры две строки
                    System.out.println("Введите имя: ");
                    String name = scan.nextLine();
                    System.out.println("Введите возраст: ");
                    int age = scan.nextInt();
                    System.out.println("Введите зарплату: ");
                    int salary = scan.nextInt();

                    workers.add(new Worker().setName(name).setAge(age).setSalary(salary));

                    //задаем запрос ЗАПОЛНЕНИЯ, как строку
                    String query2 = "INSERT INTO " + tablename +
                            " (name, age, salary)"
                            + " VALUES (?, ?, ?);";

                    //ОБЪЕКТ PreparedStatement:
                    //заранее подготавливает запрос с указанием мест, где будут подставляться параметры (знаки '?')
                    //устанавливает параметры определенного типа
                    //и выполняет после этого запрос с уже установленными параметрами
                    //МЕТОД prepareStatement ОБЪЕКТА PreparedStatement
                    PreparedStatement stmt3 = con2.prepareStatement(query2);

                    //установка параметров
                    //МЕТОД setString ОБЪЕКТА PreparedStatement
                    stmt3.setString(1, name);
                    stmt3.setInt(2, age);
                    stmt3.setInt(3, salary);

                    //выполнение запроса
                    //вызов stmt.executeUpdate() выполняется уже без указания строки запроса
                    //МЕТОД executeUpdate ОБЪЕКТА PreparedStatement
                    stmt3.executeUpdate();

                    System.out.println("Данные в MySQL успешно внесены!");

                    //ResultSet - ОБЪЕКТ java, содержащий результаты выполнения sql запросов
                    //executeQuery - МЕТОД ОБЪЕКТА ResultSet
                    ResultSet rs2 = stmt3.executeQuery("SELECT * FROM " + tablename + "");
                    System.out.println("Введенные данные: ");

                    //ОБЪЕКТ Statement для выполнения sql запросов
                    //МЕТОД createStatement ОБЪЕКТА PreparedStatement
                    Statement statement = con2.createStatement();

                    //ResultSet - ОБЪЕКТ java, содержащий результаты выполнения sql запросов
                    //МЕТОД executeQuery ОБЪЕКТА Statement
                    ResultSet set = statement.executeQuery("SELECT * FROM " + tablename + " LIMIT 0;");

                    //ОБЪЕКТ ResultSetMetaData содержит информацию о результирующей таблице
                    //- количество колонок, тип значений колонок и т.д.
                    //МЕТОД getMetaData ОБЪЕКТА ResultSet
                    ResultSetMetaData data = set.getMetaData();

                    //определяем количество колонок
                    //МЕТОД getColumnCount ОБЪЕКТА ResultSetMetaData
                    int cnt = data.getColumnCount();

                    //выводим названия колонок через пробел
                    //цикл с фиксированным количествоом повторений от i = 1 до i = cnt
                    for (int i = 1; i <= cnt; i++) {
                        System.out.print(data.getColumnName(i) + " ");
                    }
                    System.out.print("\n");

                    //МЕТОД rs2.next() - построчный вывод введенных данных в цикле
                    while (rs2.next()) {
                        for (int i = 1; i <= cnt; i++) {
                            System.out.print(Arrays.toString(rs2.getString(i).split("   ")));
                        }
                        System.out.println();
                    }

                    ///вывод количества строк в таблице
                    //создаем sql запрос
                    String query = "select count(*) from " + tablename;

                    //пробуем выполнить запрос через try - catch
                    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "кщще");
                         Statement stmt = con.createStatement();
                         ResultSet rs = stmt.executeQuery(query)) {
                        while (rs.next()) {
                            int count = rs.getInt(1);
                            System.out.println("Всего внесено строк в таблицу " + tablename + " : " + count);
                        }
                    } catch (SQLException sqlEx) {
                        sqlEx.printStackTrace();
                    }
                }
                //если пользователь ввел цифру 4, то...
                case 4 -> {
                    EXL.ExcelConvector(tablename);
                }
            }

            //если пользователь введет 5, то выйдет из программы
            System.out.println("Вышли из нашей программы.");
        }
    }
}

/*
List<Worker> workers = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        System.out.println("Введите имя: ");
        String name = scan.nextLine();
        System.out.println("Введите возраст: ");
        int age = scan.nextInt();
        System.out.println("Введите зарплату: ");
        int salary = scan.nextInt();

        workers.add(new Worker().setName(name).setAge(age).setSalary(salary));
        System.out.println(workers);
 */
