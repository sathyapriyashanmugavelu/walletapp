# Setup service

## Install java
- Install jdk11
- Verify installed java version by running command
```shell script
java -version
javac -version
```

## Install postgres

## Run the application
- Build the application and run the test along with coverage
```shell script
./gradlew build
```
- Run the server locally on localhost:8080
```shell script
./gradlew bootRun
```

- Setup Postgres using docker
    - Install Dockerhub for mac
    - Create an account in the dockerhub and login
    - Start db command
    `docker run --name postgresdb -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres`
    - Install below tools to connect to the database
    ```
       brew cask install pgadmin4   
       brew install psqlodbc
    ```
    - Connect to db command
    `psql -h localhost -U postgres -d postgres`
    
- Running app in Intellij
    - Set spring profile in Edit configurations VM Options: 
    `-Dspring.profiles.active=dev`  
    
- Heroku deployment
    - install heroku CLI `brew tap heroku/brew && brew install heroku`
    - Login to Heroku
    `heroku login` 
    - `git init` from the project directory if not git initialized
    -  commit latest `gc -m 'enter comments here'`
    - create a heroku app `heroku create`
    - Install Add-on 'Heroku Postgres' on the deployed heroku app 
    - `git push heroku master`
    - Currently accessible in https://shrouded-oasis-00998.herokuapp.com/
