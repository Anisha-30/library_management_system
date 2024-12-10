import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.Scanner;
import java.io.*;

public class LibraryManagement {
    private static ArrayList<HashMap<String, String>> books = new ArrayList<>();
    private static ArrayList<HashMap<String, String>> lib_members = new ArrayList<>();
    private static ArrayList<HashMap<String, String>> borrowedBook = new ArrayList<>();
    private static HashMap<String, HashMap<String, Integer>> bookCount = new HashMap<>();

    public static void displayMenu() {
        System.out.println("*****Welcome to Library***");
        System.out.println("Please Choose the Option -");
        System.out.println("1. Add new books.\r\n" +
                "2. Register new library members.\r\n" +
                "3. Borrow a book.\r\n" +
                "4. Return a book.\r\n" +
                "5. View all available books.\r\n" +
                "6. View all registered members.\r\n" +
                "7. Search books by Title/Author.\r\n" +
                "8. View borrowed books.\r\n" +
                "9. Exit.");

    }

    public static void addBooks() {
        Scanner sc1 = new Scanner(System.in);
        System.out.println("1. Add new books");
        System.out.println("2. Update old book stock");
        System.out.print("Enter choice: ");

        int input = sc1.nextInt();
        switch (input) {
            case 1:
                add_newBook();
                break;

            case 2:
                update_bookStock();
                break;

            default:
                System.out.println("INVALID CHOICE");

        }

        // books.add("The guide");
        // books.add("The Post Office");
        // books.add("The Great Indian Novel");
        // books.add("Broken Wings");
        // books.add("Bunch of old letters");
    }

    public static void add_newBook() {
        try {
            HashMap<String, String> book = new HashMap<>();
            HashMap<String, Integer> NoOfBook = new HashMap<>();
            Scanner sc = new Scanner(System.in);
            System.out.print("enter the title of book: ");
            String Title = sc.nextLine();
            book.put("Title", Title);
            System.out.print("enter the author name of book: ");
            book.put("Author", sc.nextLine());
            String bookID = UUID.randomUUID().toString();
            bookID = bookID.replaceAll("-", "").substring(0, 2);
            book.put("bookId", bookID);

            System.out.print("Set total no. of book: ");
            Integer totbook = sc.nextInt();
            NoOfBook.put("totbook", totbook);
            books.add(book);
            bookCount.put(bookID, NoOfBook);
            System.out.println("book added successfully- book id: " + bookID);
            System.out.println(bookCount);

            FileWriter fw = new FileWriter("booksfile.txt", true);
            fw.write(book.get("Title") + ":" + book.get("Author") + ":" + book.get("bookId") + "\r\n");
            fw.close();

            FileWriter fw1 = new FileWriter("totalBook.txt", true);
            fw1.write(bookID + ":" + NoOfBook.get("totbook") + "\r\n");
            fw1.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void update_bookStock() {
        try {

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter bookid to be updated: ");
            String bookID = sc.nextLine();

            // Update book count
            HashMap<String, Integer> NoOfBook = bookCount.get(bookID);

            int available = NoOfBook.get("totbook") + 1;
            NoOfBook.put("totbook", available);
            bookCount.put(bookID, NoOfBook);
            System.out.println("Book updated succesfully.");

            // file update...
            FileWriter fw1 = new FileWriter("totalBook.txt");
            for (String bookId : bookCount.keySet()) {
                fw1.write(bookId + ":" + bookCount.get(bookId).get("totbook") + "\r\n");
            }
            fw1.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void addMembers() {

        try {
            Scanner sc = new Scanner(System.in);
            HashMap<String, String> members = new HashMap<>();
            System.out.print("enter the member name: ");
            members.put("Name", sc.nextLine());
            String userID = UUID.randomUUID().toString();
            userID = userID.replaceAll("-", "").substring(0, 3);
            members.put("userId", userID);

            lib_members.add(members);
            System.out.println("New member added successfully of member ID: " + userID);

            FileWriter fw1 = new FileWriter("membersfile.txt", true);
            fw1.write(members.get("Name") + ":" + members.get("userId") + "\r\n");
            fw1.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void borrowBook() {
        try {
            Scanner sc1 = new Scanner(System.in);
            System.out.print("Enter no. of books to borrow: ");
            int n = sc1.nextInt();

            Scanner sc2 = new Scanner(System.in);
            System.out.print("give your user id: ");
            String userID = sc2.nextLine();

            for (int i = 1; i <= n; i++) {
                Scanner sc = new Scanner(System.in);
                System.out.print("enter the book ID " + i + " for book borrow: ");
                String bookID = sc.nextLine();
                HashMap<String, String> borrowDetails = new HashMap<>();
                HashMap<String, Integer> NoOfBook = new HashMap<>();

                NoOfBook = bookCount.get(bookID);
                Integer available = NoOfBook.get("totbook");
                System.out.println(available);
                if (available > 0) {
                    bookCount.remove(bookID);
                    NoOfBook.put("totbook", available - 1);
                    bookCount.put(bookID, NoOfBook);
                    System.out.println("current book count " + bookCount);
                    boolean bookFound = false;
                    boolean memberFound = false;
                    for (HashMap<String, String> book : books) {
                        if (book.get("bookId").equals(bookID)) {
                            bookFound = true;
                            for (HashMap<String, String> members : lib_members) {
                                if (members.get("userId").equals(userID)) {
                                    memberFound = true;
                                    borrowDetails.put("bookId", bookID);
                                    borrowDetails.put("userId", userID);
                                    borrowDetails.put("status", "borrowed");
                                    borrowedBook.add(borrowDetails);
                                    //borrowedBook.put(bookID, borrowDetails);
                                    System.out.println("Book borrowed successfully.");
                                    System.out.println(borrowedBook);

                                    // Update the file with borrowed book details
                                    FileWriter fw2 = new FileWriter("libraryData.txt", true);
                                    fw2.write(borrowDetails.get("bookId") + ":" + borrowDetails.get("userId") + ":"
                                            + borrowDetails.get("status") + "\r\n");
                                    fw2.close();

                                }
                            }
                        }
                    }
                    if (bookFound == false) {
                        System.out.println("Book not found.");
                    } else if (memberFound == false) {
                        System.out.println("User  not registered.");
                    }

                    // update total book file..
                    FileWriter fw1 = new FileWriter("totalBook.txt");
                    for (String bookId : bookCount.keySet()) {
                        fw1.write(bookId + ":" + bookCount.get(bookId).get("totbook") + "\r\n");
                    }
                    fw1.close();

                } else {
                    System.out.println("No more copies of book is available.");
                    return;
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void returnBook() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter the number of books to return: ");
            int num = sc.nextInt();
            Scanner sc1 = new Scanner(System.in);
            System.out.print("Enter your User ID: ");
            String userID = sc1.nextLine();

            for (int i = 1; i <= num; i++) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter the Book ID to return: ");
                String bookID = scanner.nextLine();
                boolean userFound = false;
                for(int id =0; id<borrowedBook.size();id++){
                    HashMap<String, String> borrowDetails = borrowedBook.get(id);
                    if(borrowDetails.get("userId").matches(userID) && 
                    borrowDetails.get("bookId").matches(bookID)){
                        userFound = true;
                        borrowedBook.remove(id);                        
                        id=0;
                    }
                }
                
                if(userFound == false) {
                    System.out.println("Book ID not found or doesn't borrowed by you.");
                    return;
                }


                    // Update book count
                    HashMap<String, Integer> NoOfBook = bookCount.get(bookID);
                    if (NoOfBook != null) {
                        int available = NoOfBook.get("totbook") + 1;
                        NoOfBook.put("totbook", available);
                        bookCount.put(bookID, NoOfBook);
                    }

                    // File update
                    FileWriter fw2 = new FileWriter("libraryData.txt");
                    for (HashMap<String, String> borrowUpdate : borrowedBook) {
                        fw2.write(borrowUpdate.get("bookId") + ":" + borrowUpdate.get("userId") + ":"
                                + borrowUpdate.get("status") + "\r\n");
                    }

                    fw2.close();

                    // file update...
                    FileWriter fw1 = new FileWriter("totalBook.txt");
                    for (String bookId : bookCount.keySet()) {
                        fw1.write(bookId + ":" + bookCount.get(bookId).get("totbook") + "\r\n");
                    }
                    fw1.close();

                    System.out.println("Book returned successfully.");

            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void printAvailableBooks() {

        if (books.size() > 0) {
            System.out.println("------All available books-------");
            int borrowedCount = 0;
            
            for (HashMap<String, String> book : books) {
                String bookID = book.get("bookId");
                int available = bookCount.get(bookID).get("totbook");
                for (HashMap<String, String> borrowDetails : borrowedBook) {
                    if (borrowDetails.get("bookId").equals(bookID) 
                                      && borrowDetails.get("status").equals("borrowed")) {
                        borrowedCount++;
                    }
                }
                int totalBook = available + borrowedCount;
                System.out.println("Title: " + book.get("Title") + ", Author: " + book.get("Author")
                        + ", BookId: " + book.get("bookId") + ", available: " + available
                        + ", total book: " +totalBook );
            }

        } else {
            System.out.println("No book in catalog.");
        }

    }

    public static void printRegisturedMembers() {
        if (lib_members != null) {
            System.out.println("--------All Registured members-----");
            for (HashMap<String, String> members : lib_members) {
                System.out.println("Member name: " + members.get("Name") + ", user Id: " + members.get("userId"));
            }

        } else {
            System.out.println("No members are registured.");
        }
    }

    public void searchBooks() {
        Scanner sc = new Scanner(System.in);
        System.out.print("enter book title or author: ");
        String getTitle = sc.nextLine();
        int count = 0;
        for (HashMap<String, String> book : books) {
            if (book.get("Title").contains(getTitle) || book.get("Author").contains(getTitle)) {
                System.out.println("Title: " + book.get("Title") + ", Author: " + book.get("Author") + ", book Id: "
                        + book.get("bookId"));
                count++;
            }
        }
        if (count == 0) {
            System.out.println("Book not available");
        }

    }

    public static void printBorrowedBooks() {

        if (!borrowedBook.isEmpty()) {
            System.out.println("-----All borrowed books------");
            for (HashMap<String, String> borrowDetails : borrowedBook) {
                if (borrowDetails.get("status").equals("borrowed")) {
                    System.out.println(
                            "book id: " + borrowDetails.get("bookId") + ", userId: " + borrowDetails.get("userId")
                                    + ", status: " + borrowDetails.get("status"));
                }
            }

        } else {
            System.out.println("No book borrowed");
        }
    }

    public static void getFileData() {
        // read book data
        FileReader fr;
        try {
            fr = new FileReader("booksfile.txt");
            Scanner scanner = new Scanner(fr);
            while (scanner.hasNextLine()) {
                HashMap<String, String> book = new HashMap<>();
                String data = scanner.nextLine();
                String[] line = data.split(":");
                book.put("Title", line[0]);
                book.put("Author", line[1]);
                book.put("bookId", line[2]);
                books.add(book);
            }
            scanner.close();

            // read members data
            FileReader fr1 = new FileReader("membersfile.txt");
            Scanner scanner2 = new Scanner(fr1);

            while (scanner2.hasNextLine()) {
                HashMap<String, String> members = new HashMap<>();
                String data = scanner2.nextLine();
                String[] line = data.split(":");
                members.put("Name", line[0]);
                members.put("userId", line[1]);
                lib_members.add(members);

            }
            scanner2.close();

            // read borrowed book data
            FileReader fr2 = new FileReader("libraryData.txt");
            Scanner scanner3 = new Scanner(fr2);

            while (scanner3.hasNextLine()) {
                HashMap<String, String> borrowDetails = new HashMap<>();
                String data = scanner3.nextLine();
                // System.out.print(data);
                String[] line = data.split(":");
                borrowDetails.put("bookId", line[0]);
                borrowDetails.put("userId", line[1]);
                borrowDetails.put("status", line[2]);
                //borrowedBook.put(line[0], borrowDetails);
                borrowedBook.add(borrowDetails);

            }
            scanner3.close();

            // read totalBook data
            FileReader fr3 = new FileReader("totalBook.txt");
            Scanner scanner4 = new Scanner(fr3);
            while (scanner4.hasNextLine()) {
                HashMap<String, Integer> NoOfBook = new HashMap<>();
                String data = scanner4.nextLine();
                String[] line = data.split(":");
                String bookId = line[0];
                NoOfBook.put("totbook", Integer.parseInt(line[1]));
                bookCount.put(bookId, NoOfBook);
                // System.out.println(bookCount);

            }
            scanner3.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        getFileData();

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        LibraryManagement lib = new LibraryManagement();
        while (running) {
            displayMenu();
            int input = sc.nextInt();

            switch (input) {
                case 1:
                    lib.addBooks();
                    break;
                case 2:
                    lib.addMembers();
                    break;
                case 3:
                    lib.borrowBook();
                    break;
                case 4:
                    lib.returnBook();
                    break;
                case 5:
                    lib.printAvailableBooks();
                    break;
                case 6:
                    lib.printRegisturedMembers();
                    break;
                case 7:
                    lib.searchBooks();
                    break;
                case 8:
                    lib.printBorrowedBooks();
                    break;
                case 9:
                    running = false;
                    System.out.println("Exiting the Library.");
                    break;
                default:
                    System.out.println("INVALID CHOICE");
            }
        }

    }

}
