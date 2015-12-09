USE v22;

-- Drop tables
DROP TABLE IF EXISTS AuthenticationState;
DROP TABLE IF EXISTS AuthenticatedUser;
DROP TABLE IF EXISTS User;

-- Create tables

CREATE TABLE User (
  id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  userName         VARCHAR(255) UNIQUE NOT NULL,
  name             VARCHAR(255)        NOT NULL,
  surName          VARCHAR(255)        NOT NULL,
  idCode           VARCHAR(11)  UNIQUE NOT NULL,
  googleID         VARCHAR(255) UNIQUE,
  facebookID       VARCHAR(255) UNIQUE,
  role             VARCHAR(255)        NOT NULL
);

CREATE TABLE AuthenticatedUser (
  id                 BIGINT  AUTO_INCREMENT PRIMARY KEY,
  user_id            BIGINT              NOT NULL,
  token              VARCHAR(255) UNIQUE NOT NULL,
  firstLogin         BOOLEAN DEFAULT FALSE,
  homeOrganization   VARCHAR(255),
  mails              VARCHAR(255),
  affiliations       VARCHAR(255),
  scopedAffiliations VARCHAR(255),

  FOREIGN KEY (user_id)
  REFERENCES User (id)
    ON DELETE RESTRICT
    ON DELETE CASCADE
);

CREATE TABLE AuthenticationState (
  id          BIGINT    AUTO_INCREMENT PRIMARY KEY,
  token       VARCHAR(255) UNIQUE NOT NULL,
  created     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  name        VARCHAR(255),
  surname     VARCHAR(255),
  idCode      VARCHAR(11),
  sessionCode VARCHAR(255)
);
