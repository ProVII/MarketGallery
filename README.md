This is a project submission to CSCI 4100U Mobile Devices entitled "Snackies Market Gallery".

#### SUBMITTED BY:
- Fuad Tareq, 100504146
- Mohammad Faraz Ali, 100490152



#### SETUP:
This project is best ran on a Nexus 5 API 23 x86 emulator (currently supports APIs 19-23), it also requires a minimum of 25 MB on your phone since it incorporates a demo video.

**Note:** If required to change the map API, you can do so by obtaining an API and entering it in the manifest directly. However you will not likely need to do so, since the API was already obtained for you, and should remain active unless disabled.

**Note:** If you are already able to run the app, you'll be able to access the demo video located on the app's login page bottom left, prior to logging in. If you would like to skip reading this text file and go ahead and watch the tutorial video, feel free to do so since it will incorporate most of the details mentioned here. However, for easier access, you'll also find the credentials for logging into the app as a developer (which are mentioned in the demo video) in here below.



#### BRIEF APP HISTORY:
Now that we're past setting up and ready to launch the app, welcome to our Snackies Market Gallery app. As the title mentions, this app is intended as a gallery viewer for Snackies branch stores. Snackies is a Canadian multi-branch company, with 7 total branches in Canada and 1 recently installed branch in the US.



#### LOGIN INSTRUCTIONS:
There are two ways to login into the app, as a Developer or as a Customer. To log in as a developer, use the credentials given below:

- Username: Admin
- Password: owner

To login as a customer, you'd have to register through the app since there are currently no customers registered.



#### DETAILED FEATURES AND FUNCTIONALITIES:
This section lists all the features included in the app alongside the concepts learned and achieved through the course.

Logging in as a Developer/Owner provides you with the ability to add/delete products from your app, allowing you to edit the products for your customers. To log in as a developer:

1. Just enter the credentials found above into the username and password fields respectively.
2. You'll notice two main differences, the menu now incorporates the ability to add or delete products, as well as, the app recognizes you logging in as a developer.
3. To delete a product, just press the "Delete this Product" from the menu.
4. To add a product, just press the "Add Product" from the menu.
  - Enter the name, description, and price of the product you wish to add.
  - |Optional| press CANCEL if you wish to go back without adding any new product.
  - Press SAVE to add the new product into the database.
5. The rest of the functionality is similar to the customer functionality and you can move to step 14 of the customer functionalities to follow.

Initially we're going to demonstrate the app when logging in as a customer:

1. Pressing on the "Devs & Owners Only!" link at the bottom left of the app allows you to view a tutorial demo video for using the app.
2. _**Notice**_ that you cannot press the LOGIN button unless you have entered something into both text fields.
3. _**Notice**_ that if you try to enter a non-existing username it will display an error.
4. To login as a customer, you'll need to register first, so press the REGISTER button to proceed.
5. _**Notice**_ should you decide to carry on to Register after entering a username that doesn't exist, the Login page will pass your entered username to the Registration page to lessen the time of entering a username when registering.
6. Enter a name, a username (if above step didn't already provide your username), a password, re-enter password, and email address.
7. _**Notice**_ that if you attempt to leave any field blank you'll receive an error.
8. --Note-- that the username and password fields are case sensitive. As well as, you must ensure the password and Confirm Password fields do match or you'll recieve an error.
9. --Note-- that the email address must be valid, if the email address doesn't contain the "@" and "." characters, you'll get an error.
10. --Note-- if the username already exists in the database, you'll not be able to register and will receive an error to change it.
11. Press the REGISTER button and it will take you back to the login page with a toast message displaying "Successfully Registered!". If you're unable to register, please do check steps 7-10 for a successful registration.
12. Enter your registered username (if it's not still there) and your registered password.
13. |Optional| You may logout from the menu at any time.
14. _**Notice**_ that your username is displayed on the top left, next to your profile picture (currently set to default).
15. Press your profile picture to access your profile, OR press My Profile from the menu.
16. _**Notice**_ Your registered account credentials will be displayed below your picture.
17. Press the picture to change it, then select a picture from your gallery.
18. _**Notice**_ that if you don't have any pictures currently stored on your phone, have no fear, the app creates four pictures on your device by default on installation to switch between.
19. _**Notice**_ that your picture size may get resized to fit into the screen.
20. |Optional| You may logout from the menu at any time.
21. Press BACK to switch to your current selected profile picture.
22. _**Notice**_ that you may go back to your profile again at any time (either by pressing on your profile picture or via the menu) and change the picture again to a different picture.
23. _**Notice**_ that the "Snackies Market Gallery" logo was drawn using 2D Graphics with an external typeface font.
24. _**Notice**_ that by default the products are dispalyed by name, description, CAD price, and USD price. The last currency can be switched however to any currency you may like, by default it is in USD since we've just opened our first US branch.
25. Press "Different currency?" to change currency.
26. _**Notice**_ that you may select any currency from the dropdown menu to switch to.
27. |Optional| you may go back by pressing CANCEL without making any changes to the currency.
28. Press SWITCH CURRENT CURRENCY to change the app's default displayed currency (USD) to the selected currency.
29. Press NEXT to view the next product.
30. _**Notice**_ the magic of all products displaying converted price to your currency now.
31. _**Notice**_ that the PREVIOUS button is unclickable when it's on the first product, and the same applies for the NEXT button, to improve user error detection.
32. Press Contact Us through the menu to view our current known branch locations and find the nearest location to you.
33. --Note-- if the map complains about the API, please check the SETUP section (first note) of the README file (found at the top).
34. _**Notice**_ we'll find the nearest branches to you based on your current GPS location. Normally, this is provided automatically by your phone if there's network connection and there's no work on your end required.
35. --Note-- if you're using an emulator you'll need to send a GPS location first to your emulator for the map to work correctly as intended. Below are the necessary steps required to supply your emulator with a GPS coordinate:
  - Open Android Device Monitor, either by pressing the icon next to the "?" or by locating it through the menu Tools>Android>Android Device Monitor.
  - Once opened, select the Emulator Control tab.
  - Under Location Controls, Select Manual tab.
  - Select Decimal
  - By default, there's already a longitude/latitude values entered. If there isn't, add any latitude/longitude, preferrably in North America.
  - Press Send.
36. _**Notice**_ now that your GPS location has been updated on the emulator's map just like a phone would update the GPS location, except in this case your location would depend on whatever latitude/longitude values you've entered.
37. |Optional| feel free to repeat step 35 with a new latitude/longitude location.
38. You may logout at any point now via the menu.

#### FOR APK:
If you wish to obtain a signed APK of this app to run on your android device instead of an emulator, you will find one under MarketGallery>app>app-release.apk


#### EQUAL CONTRIBUTION STATEMENT:
We've not included any names on any classes since we've both equally contributed towards this project. Thank you for your understanding and consideration.


#### LEGEND:
- _**Notice**_ - usually represents an app feature to take note of.
- --Note-- - describes user feedback for successful completion of certain steps.
- |Optional| - displays an optional step.
