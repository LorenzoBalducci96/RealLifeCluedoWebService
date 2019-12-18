# RealLifeCluedoWebService
A web service for play cluedo in real life inside a campus/hotel/home.

The rules of the game are simple.
Each session has a start date adn a end date (usually a session last weeks or months...).
In each session multiple users are trying to complete their missions.
A mission consist in touching a defined opponent in a defined place with a defined object.
Objects and places are put in the database by the administrator that use the administrator.html page (for each object/place the administrator also insert images, descriptions and points multiplicators).
Users can be registered by themself of by the administrator (that will also put them into a session).
Once a user succesfull the login, if is on a session he will be able to select that, to get all the data regarding his mission and other.
Once a user succesfully touched his user target with the required object in the required place, he will earn points depending on the mission difficult that had (100 points * place_multiplicator * object_multiplicator).
It's obvious that the more a place/object is hard to reach/use, the higher is the multiplicator (usually between 0.5 and 2).
After that, the user gets another mission.

widgets page of the adminLTE template is used both for the admin page and for the user page.
