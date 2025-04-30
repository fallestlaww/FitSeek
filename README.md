# FitSeek
FitSeek - RESTful web application, created for formation training program for user based on user age, user weight and user gender with recommendations for every exercise, formed for every category. Besides this, application can show recommended exercises for special gender of the user (male/female), output information about special type of training (split/fullbody), perform CRUD operation for different entities, namely, [User](src/main/java/org/example/fitseek/model/User.java), [Exercise](src/main/java/org/example/fitseek/model/Exercise.java) and [Recommendation](src/main/java/org/example/fitseek/model/Recommendation.java).
## Attention!
This project does not carry any serious intentions to develop further, only in case the author wants to test something new. Project also has front-end part - [link](https://github.com/fallestlaww/FitSeek-frontend?tab=readme-ov-file).
## How to work?
There is two ways to work with this project. First - clear backend.  
To work with this project in this way you must have a Postman, to get and post your data and one of various types of SQL-databases.

### For what this project needs Postman?
This project requires this application for methods which requires API request.  
So, first of all open "Postman" and choose an option "New request" just like in the example below.
![image](https://github.com/user-attachments/assets/f0f2c871-bee2-44d3-ae4e-24fccb4cfcd5)
Before all, you must get your JWT. So, first of all you need to register your profile(or authenticate if you already have one).
![image](https://github.com/user-attachments/assets/8a72f475-d1c5-4802-9e0e-898ba50327aa) Add this JWT to header as on the example: 
![image](https://github.com/user-attachments/assets/a70791b4-4911-4190-b9e5-acd72e7da564)

After that, you can work correctly with all breakpoints. If you did not do that - you will catch 401 HTTP status.
Now you can input in an URL(URL for every method you can find in the annotation above the method declaration) and choose HTTP method for a request(HTTP method for for every method you can find in the annotation above the method declaration).
#### Attention! Check if the method have a @RequestParam or @RequestBody in parameters
If the method has @RequestParam, you must to add your values for this parameters, just like in the example:
![image](https://github.com/user-attachments/assets/d4c56d7f-2e8e-4ec6-adf1-2a3ad556f7f4)
If the method has @RequestBody, you must to add your values in the body of the request in Postman.    
If everything was correct, you will see a notification about it, but if not, you will see another notification which will contain your error and http status.

### Second way
Second way - frontend + backend. You can start your frontend part on 3000 port and backend on 9060. After that you will have this: 
![image](https://github.com/user-attachments/assets/2be5b5d2-42e5-4bcc-a4df-9738ed2620f4)

## Ending
As for me, there is no point in adding more code, but if you want - you have every right to do so, if you find bugs - let me know, I will be happy to fix everything.

My telegram: @keepmewhoiam,  
Link for this repository: https://github.com/fallestlaww/FitSeek.git,  
My LinkedIn: [link](https://www.linkedin.com/in/pavlo-svitenko-a167152bb/).
