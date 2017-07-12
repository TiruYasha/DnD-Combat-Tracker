BEGIN TRANSACTION;
CREATE TABLE "Creature" (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`name`	TEXT NOT NULL,
	`size`	TEXT,
	`type`	TEXT,
	`subtype`	TEXT,
	`alignment`	TEXT,
	`strength`	INTEGER,
	`dexterity`	INTEGER,
	`constitution`	INTEGER,
	`intelligence`	INTEGER,
	`wisdom`	INTEGER,
	`charisma`	INTEGER,
	`challengeRating`	TEXT,
	`custom`	INTEGER NOT NULL DEFAULT 0 CHECK(custom = 0 or custom = 1)
);
CREATE TABLE `Speed` ( 
`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
`creatureId` INTEGER NOT NULL,
`name` TEXT,
FOREIGN KEY(`creatureId`) REFERENCES Creature(_id) );
CREATE TABLE `Skill` ( `_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `creatureId` INTEGER NOT NULL, `name` TEXT, `modifier` INTEGER, FOREIGN KEY(`creatureId`) REFERENCES Creature(_id) );
CREATE TABLE `Sense` ( 
`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
`creatureId` INTEGER NOT NULL,
`description` TEXT,
FOREIGN KEY(`creatureId`) REFERENCES Creature(_id) );
CREATE TABLE `SavingThrow` (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`creatureId`	INTEGER NOT NULL,
	`name`	TEXT,
	`modifier`	INTEGER,
	FOREIGN KEY(`creatureId`) REFERENCES Creature(_id)
);
CREATE TABLE `Language` ( 
`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
`creatureId` INTEGER NOT NULL,
`name` TEXT,
FOREIGN KEY(`creatureId`) REFERENCES Creature(_id) );
CREATE TABLE `HitPoint` ( 
`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
`creatureId` INTEGER NOT NULL,
`value` INTEGER,
`hitDice` TEXT, 
FOREIGN KEY(`creatureId`) REFERENCES Creature(_id) );
CREATE TABLE `DamageVulnerability` ( 
`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
`creatureId` INTEGER NOT NULL,
`name` TEXT,
FOREIGN KEY(`creatureId`) REFERENCES Creature(_id) );
CREATE TABLE `DamageResistance` ( 
`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
`creatureId` INTEGER NOT NULL,
`name` TEXT,
FOREIGN KEY(`creatureId`) REFERENCES Creature(_id) );
CREATE TABLE `DamageImmunity` ( 
`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
`creatureId` INTEGER NOT NULL,
`name` TEXT,
FOREIGN KEY(`creatureId`) REFERENCES Creature(_id) );

CREATE TABLE `ConditionImmunity` ( 
`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
`creatureId` INTEGER NOT NULL,
`name` TEXT,
FOREIGN KEY(`creatureId`) REFERENCES Creature(_id) );
CREATE TABLE `ArmorClass` ( 
`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
`creatureId` INTEGER NOT NULL,
`value` INTEGER,
`type` TEXT, 
FOREIGN KEY(`creatureId`) REFERENCES Creature(_id) );
CREATE TABLE `Action` ( 
`_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
`creatureId` INTEGER NOT NULL,
`name` TEXT,
`description` TEXT,
`attackBonus` integer,
`damageDice` TEXT,
`damageBonus` integer,
`actionCategory` TEXT,
FOREIGN KEY(`creatureId`) REFERENCES Creature(_id) );
COMMIT;
