# HungryEye
Takeaway application (android)
1. Login system on android, php and mysql on the server
2. Communication carried out using Volley library between the android client and server
3. Appcompat for job service apk level <21-- by using async task with volley (future request)
4 Menu list pulled from server and saved into SQLite database
5 If there is no network then in-build database of menu list is used
6 Custom listview implemented for the better UI
7 When clicked on listview DialogFragment opens to show customer detail of the product
8 Using interface the clicked is capture in main activity to be updated in cart
9 Cart is also a SQLite table where the selection is kept
10 When user is ready they press the buy button which takes them to the paypal login
11 After a payment the confirmation is sent to server, server sends confirmation to the android client which is saved in sqlite databse
