package com.example.courageous.marketgallery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import java.sql.SQLException;

public class DatabaseAdapter {

    DatabaseHelper DbHelper;
    SQLiteDatabase db;
    final Context context;

    public DatabaseAdapter(Context ctx) {
        this.context = ctx;
        DbHelper = new DatabaseHelper(context);
    }

    // Inner class that defines the Product table contents.
    public static abstract class ProductsTable implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME_PRODUCT_ID = "productid";
        public static final String COLUMN_NAME_PRODUCT_NAME = "productname";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_NULLABLE = "null";
    }

    // Inner class that defines the Login table contents.
    public static abstract class LoginTable implements BaseColumns {
        public static final String TABLE_NAME = "credentials";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_NULLABLE = "null";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String DECIMAL_TYPE = " DECIMAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_PRODUCTS =
            "CREATE TABLE " + ProductsTable.TABLE_NAME + " (" +
                    ProductsTable.COLUMN_NAME_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ProductsTable.COLUMN_NAME_PRODUCT_NAME + TEXT_TYPE + " not null" + COMMA_SEP +
                    ProductsTable.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    ProductsTable.COLUMN_NAME_PRICE + DECIMAL_TYPE + " not null" +
                    " )";
    private static final String SQL_CREATE_CREDENTIALS =
            "CREATE TABLE " + LoginTable.TABLE_NAME + " (" + "_id integer primary key autoincrement," +
                    LoginTable.COLUMN_NAME_USERNAME + TEXT_TYPE + " not null" + COMMA_SEP +
                    LoginTable.COLUMN_NAME_PASSWORD + TEXT_TYPE + " not null" + COMMA_SEP +
                    LoginTable.COLUMN_NAME_NAME + TEXT_TYPE + " not null" + COMMA_SEP +
                    LoginTable.COLUMN_NAME_EMAIL + TEXT_TYPE + " )";

    private static final String SQL_DELETE_PRODUCTS =
            "DROP TABLE IF EXISTS " + ProductsTable.TABLE_NAME;
    private static final String SQL_DELETE_CREDENTIALS =
            "DROP TABLE IF EXISTS " + LoginTable.TABLE_NAME;

    public class DatabaseHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Products.db";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * This onCreate method runs only on first time, creating the table and populating
         * the database with initial product data seen below.
         * @param db is a SQLite database parameter
         */
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_CREDENTIALS);
            db.execSQL(SQL_CREATE_PRODUCTS);

            ContentValues credentials = new ContentValues(); // Developer credentials
            credentials.put(LoginTable.COLUMN_NAME_USERNAME, "Admin");
            credentials.put(LoginTable.COLUMN_NAME_PASSWORD, "owner");
            credentials.put(LoginTable.COLUMN_NAME_NAME, "Snackies Owners");
            credentials.put(LoginTable.COLUMN_NAME_EMAIL, "developers@snackies.com");

            db.insert(LoginTable.TABLE_NAME, LoginTable.COLUMN_NAME_NULLABLE, credentials);

            String[] product = {"Doritos", "Ruffles", "Cheetos", "Pringles", "Lay's"};
            String[] description = {"is a brand of seasoned tortilla chips produced since 1964 by" +
                    " American food company Frito-Lay (a wholly owned subsidiary of PepsiCo).", "is" +
                    " the name of a brand of ruffled (crinkle-cut) potato chips produced by Lay's " +
                    "potato chips, a division of Frito-Lay.", "is a brand of cheese-flavored, puffed" +
                    " cornmeal snacks made by Frito-Lay, a subsidiary of PepsiCo.", "is a brand of " +
                    "potato and wheat based stackable snack chips owned by the Kellogg Company.", "is" +
                    " the brand name for a number of potato chip varieties as well as the name of " +
                    "the company that founded the chip brand in 1932."};
            Double[] price = {4.99, 10.69, 15.30, 49.99, 25.00};

            ContentValues values = new ContentValues(); /* Adding some initial products to the database
            through arrays of products. */

            for (int i=0; i < product.length; i++) {
                values.put(ProductsTable.COLUMN_NAME_PRODUCT_ID, i+1);
                values.put(ProductsTable.COLUMN_NAME_PRODUCT_NAME, product[i]);
                values.put(ProductsTable.COLUMN_NAME_DESCRIPTION, description[i]);
                values.put(ProductsTable.COLUMN_NAME_PRICE, price[i]);

                db.insert(ProductsTable.TABLE_NAME, ProductsTable.COLUMN_NAME_NULLABLE, values);
            }
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_CREDENTIALS);
            db.execSQL(SQL_DELETE_PRODUCTS);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public DatabaseAdapter open() throws SQLException {
        // Gets the data repository in write mode and a connection with the database.
        db = DbHelper.getWritableDatabase();
        return this;
    }

    // A function to close the database.
    public void close() {
        DbHelper.close();
    }

    // An array representing the Login table columns.
    String[] credentialsColumns = {LoginTable.COLUMN_NAME_USERNAME, LoginTable.COLUMN_NAME_PASSWORD,
            LoginTable.COLUMN_NAME_NAME, LoginTable.COLUMN_NAME_EMAIL};

    // A function for querying the Login table by usernames.
    public Cursor checkUser(String username) {
        String selection = DatabaseAdapter.LoginTable.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = { username };

        return db.query( // Querys the database for usernames
                LoginTable.TABLE_NAME,                                // The table to query
                credentialsColumns,                                   // The columns to return
                selection,                                            // The columns for the WHERE clause
                selectionArgs,                                        // The values for the WHERE clause
                null,                                                 // don't group the rows
                null,                                                 // don't filter by row groups
                null                                                  // Sort Order
        );
    }

    // A function for querying the Login table by emails.
    public Cursor checkEmail(String email) {
        String selection2 = DatabaseAdapter.LoginTable.COLUMN_NAME_EMAIL + " = ?";
        String[] selectionArgs2 = { email };

        return db.query( // Querys the database for usernames
                LoginTable.TABLE_NAME,                                // The table to query
                credentialsColumns,                                   // The columns to return
                selection2,                                           // The columns for the WHERE clause
                selectionArgs2,                                       // The values for the WHERE clause
                null,                                                 // don't group the rows
                null,                                                 // don't filter by row groups
                null                                                  // Sort Order
        );
    }

    // A function for inserting credential values into the Login table.
    public void insertCredential(ContentValues values) {
        db.insert( //Inserting the new product into the database
                LoginTable.TABLE_NAME,
                LoginTable.COLUMN_NAME_NULLABLE,
                values);
    }

    // An array representing the Product table columns.
    String[] productColumns = { // Initializes an array of columns
            ProductsTable.COLUMN_NAME_PRODUCT_ID, ProductsTable.COLUMN_NAME_PRODUCT_NAME,
            ProductsTable.COLUMN_NAME_DESCRIPTION, ProductsTable.COLUMN_NAME_PRICE};

    // A function for querying the products in the Product table.
    public Cursor checkProducts () {
        return db.query( // Querys the database for products
                ProductsTable.TABLE_NAME,                                // The table to query
                productColumns,                                          // The columns to return
                null,                                                    // The columns for the WHERE clause
                null,                                                    // The values for the WHERE clause
                null,                                                    // don't group the rows
                null,                                                    // don't filter by row groups
                null                                                     // Sort Order
        );
    }

    // An insert function for inserting products into the Product table.
    public void insertProduct(ContentValues values) {
        db.insert( //Inserting the new product into the database
                ProductsTable.TABLE_NAME,
                ProductsTable.COLUMN_NAME_NULLABLE,
                values);
    }

    // A delete function for deleting products from the Product table.
    public void deleteProduct(int id) {
        // Deleting an element from the database and re-querying from the database
        db.delete(ProductsTable.TABLE_NAME, ProductsTable.
                COLUMN_NAME_PRODUCT_ID + "=" + id, null);
    }
}