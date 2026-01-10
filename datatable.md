## user table
primary key: username
properties:
  - id (unique)
  - username
  - password
  - nickname
  - avatar
  - permission

## agent table
primary key: id
properties:
  - id (unique)
  - userId (foreign key to user table)
  - name
  - description
  - prompt
  - public (boolean)