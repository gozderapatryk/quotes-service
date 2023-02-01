## Run

to run application prepare the `.env` file in the root directory. The file should contains the following environment variables: 

```
POSTGRES_DB_HOST=postgresdb
POSTGRES_DB_PORT=5432
POSTGRES_DB_NAME=quotes_db
POSTGRES_DB_USER=postgres
POSTGRES_DB_PASSWORD=password
```

then execute the following command:

`docker-compose up --build`

and then you can test it over [Swagger UI](http://localhost:8080/swagger-ui/). You can also import postman's collections ([quotes-service.postman_collection.json](./quotes-service.postman_collection.json) and [quotes-service-environment.postman_environment.json](./quotes-service-environment.postman_environment.json)) which contain definitions of the request.



## Business requirements

The business requirements were described [here](./quotes-service/README.md).