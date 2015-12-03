
-- User

insert into User(id, userName, name, surName, idCode, role) values (1, 'mati.maasikas', 'Mati', 'Maasikas', '39011220011', 'USER');
insert into User(id, userName, name, surName, idCode, role) values (2, 'peeter.paan', 'Peeter', 'Paan', '38011550077', 'USER');
insert into User(id, userName, name, surName, idCode, role) values (3, 'voldemar.vapustav', 'Voldemar', 'Vapustav', '37066990099', 'USER');
insert into User(id, userName, name, surName, idCode, role) values (4, 'voldemar.vapustav2', 'Voldemar', 'Vapustav', '15066990099', 'USER');
insert into User(id, userName, name, surName, idCode, role) values (5, 'mati.maasikas2', 'Mäti', 'Maasikas', '39011220012', 'USER');
insert into User(id, userName, name, surName, idCode, role) values (6, 'mati.maasikas-vaarikas', 'Mati', 'Maasikas-Vaarikas', '39011220013', 'USER');

-- AuthenticatedUser

insert into AuthenticatedUser(id, user_id, token, firstLogin, homeOrganization, mails, affiliations, scopedAffiliations) values (1, 1, 'token', false, null, null, null, null);

-- AuthenticationState

insert into AuthenticationState(id, token) values (1, 'testTOKEN');
insert into AuthenticationState(id, token) values (2, 'taatAuthenticateTestToken');
