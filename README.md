"Automated selection of materials for the repair of an apartment" is intended to summarize information 
about the availability of certain building materials that may be necessary for the repair of an apartment, 
depending on the desires and needs of a particular client. The users of the program are the foreman, the 
apartment's owner and the deliveryman.

The program is written on the basis of the "JavaFX", a Java-based platform for building rich GUI applications, 
and the "JFoenix", an open source java library, that implements Google Material Design using java components.
Also in the project was used database management system "MySQL".

The project structure is given below:

-project<br>
--- animations<br>
--- assets<br>
--- controllers<br>
--- database<br>
--- layouts<br>
--- listeners<br>
--- materials<br>
--- models<br>
--- styles<br>
--- Main<br>

The animations package is used to reply to the user's actions. For example, in order to show that the 
password entered by the user is incorrect, the program needs to highlight this field. For this reason shaking
animation was added. 

The assets package stores all png images that have been used to create a beautiful and intelligible user interface.

In the controllers package are all the java classes that give functionality and logic to the program. 

The database package serves as a connection between system's database and the program itself. All adding, deleting,
updating and selecting requests to the database are written in this package's java class.

The layouts package stores all the fxml files, that are responsible for rendering the design of the system.

The listeners package has the interfaces, used to track user actions. Methods of these interfaces open pages to the 
user after clicking on certain buttons.

In the materials package are the images of the building materials, that were used to create a catalog of materials for the owner.

The models package has java classes, that describe entities such as apartment, material and user.

The styles package makes the interface elements more beautiful and adds to them different king of effects.

Main is a java class, that launches the program. 