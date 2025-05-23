Senior Spring Boot Developer Coding Challenge
Background
You are tasked with building a Restaurant Review API. This system will manage basic restaurant information and customer reviews.

Swagger local Link
http://localhost:8081/swagger-ui/index.html#/

===================================================================CURL for Postman testing ===================================================================

curl --location 'http://localhost:8081/auth/add_user' \
--header 'Authorization;' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name":"vinay chandekar",
    "email":"vinay@gmail.com",
    "password":"test",
    "roles":"ROLE_ADMIN"
}'

==============================================================================================================

curl --location 'http://localhost:8081/auth/authenticate' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username":"vinay@gmail.com",
    "password":"test"
}'

==============================================================================================================

curl --location 'http://localhost:8081/api/restaurants' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aW5heUBnbWFpbC5jb20iLCJpYXQiOjE3NDc5ODg2NzEsImV4cCI6MTc0Nzk5MDQ3MX0.jyKmY7UYDvmA_za0gor-szmd2UP3ED45lHG9arjsLLE' \
--data '{
  "name": "The Spice Garden-4",
  "cuisineType": "Indian",
  "address": "123 Curry Lane, Food City-4",
  "priceRange": "MEDIUM"
}
'

==============================================================================================================

curl --location 'http://localhost:8081/api/reviews' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aW5heUBnbWFpbC5jb20iLCJpYXQiOjE3NDc5OTE5MTQsImV4cCI6MTc0Nzk5MzcxNH0.x21NoT7oZq1AsVTqmQsbcarM_wiWRbXCVBJoF9SYc5Y' \
--data '{
  
    "restaurantId": 4,
  "rating": 1,
  "comment": "Excellent food and cozy ambiance!",
  "visitDate": "2024-12-15",
  "status": "APPROVED"
}
'

==============================================================================================================

curl --location 'http://localhost:8081/api/reviews/restaurant/2' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aW5heUBnbWFpbC5jb20iLCJpYXQiOjE3NDc5ODkyNDQsImV4cCI6MTc0Nzk5MTA0NH0.7F8vnjWOLy6zfYF3D8_ljkl1uGGTqP5ktrmSOwt-WSg'

==============================================================================================================

curl --location --request PUT 'http://localhost:8081/api/reviews/5/approve' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aW5heUBnbWFpbC5jb20iLCJpYXQiOjE3NDc5ODkyNDQsImV4cCI6MTc0Nzk5MTA0NH0.7F8vnjWOLy6zfYF3D8_ljkl1uGGTqP5ktrmSOwt-WSg'

==============================================================================================================

curl --location --request PUT 'http://localhost:8081/api/reviews/approve-all' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aW5heUBnbWFpbC5jb20iLCJpYXQiOjE3NDc5ODkyNDQsImV4cCI6MTc0Nzk5MTA0NH0.7F8vnjWOLy6zfYF3D8_ljkl1uGGTqP5ktrmSOwt-WSg'

==============================================================================================================

curl --location 'http://localhost:8081/api/restaurants/top3?cuisine=Indian' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aW5heUBnbWFpbC5jb20iLCJpYXQiOjE3NDc5ODkyNDQsImV4cCI6MTc0Nzk5MTA0NH0.7F8vnjWOLy6zfYF3D8_ljkl1uGGTqP5ktrmSOwt-WSg'

==============================================================================================================

curl --location 'http://localhost:8081/api/restaurants/1/average-rating' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aW5heUBnbWFpbC5jb20iLCJpYXQiOjE3NDc5ODkyNDQsImV4cCI6MTc0Nzk5MTA0NH0.7F8vnjWOLy6zfYF3D8_ljkl1uGGTqP5ktrmSOwt-WSg'

==============================================================================================================
