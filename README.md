Alternate Lastpass Keyboard for Android
========
An alternate keyboard for lastpass which instantly switches back to your default keyboard after entering the username/password.

Expected to be used with the 'switch to other input method' button on the stock android keyboard, or equivalent, for a single-button password entry which doesn't require you to manually switch back to a usable keyboard afterwards.


I forked from nhinds, included all the libraries because it's easier for me to build with eclipse.


To build import the project with eclipse.


LoginActivityTest is an effort to automate test the application but so far I run into some problems
emulating a long press on the space bar in the android keyboard.

What works:
------------


The main application is working, tested also with google OTP authenticator. Sometimes it can take a
long time to decrypt the lastpass blob.

I don't recommend you to tick the "save password option" if you want to be fully secure because this
will save the password in the android preferences which can be compromised if your phone is rooted
and another application reads the password from the storage.


TODO:
-------

If you want to help, it would be great to improve the automated tests. Given the nature of the app is quite
tedious to test.
