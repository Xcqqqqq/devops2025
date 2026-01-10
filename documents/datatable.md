## user table
primary key: username
properties:
  - id (unique)
  - username
  - password
  - nickname
  - avatar
  - permission（int, 0 or 1, 1 for admin, 0 for normal user）

## agent table
primary key: id
properties:
  - id (unique)
  - userId (foreign key to user table)
  - name
  - description
  - prompt
  - isPublic (int, 0 or 1, 1 for public, 0 for private)


## chat_session table
primary key: id
properties:
  - id (unique)
  - userId (foreign key to user table)
  - title
  - agentId (foreign key to agent table)
  - messageCount (int, default 0)
  - createdAt (datetime, default CURRENT_TIMESTAMP)
  - updatedAt (datetime, default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)


## chat_message table
primary key: id
properties:
  - id (unique)
  - sessionId (foreign key to chat_session table)
  - role (string, e.g. "user" or "assistant")
  - content
  - model
  - time (datetime, default CURRENT_TIMESTAMP)

