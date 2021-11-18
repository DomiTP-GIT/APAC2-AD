CREATE SCHEMA IF NOT EXISTS `BDJocs` DEFAULT CHARACTER SET utf8;
USE `BDJocs`;

CREATE TABLE IF NOT EXISTS `BDJocs`.`jugador`
(
    `id`           INT         NOT NULL,
    `nick`         VARCHAR(45) NULL,
    `dataRegistre` DATETIME    NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `BDJocs`.`Genere`
(
    `id`         INT         NOT NULL,
    `nom`        VARCHAR(45) NULL,
    `descripció` VARCHAR(45) NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `BDJocs`.`Joc`
(
    `id`         INT         NOT NULL,
    `nom`        VARCHAR(45) NULL,
    `descripció` VARCHAR(45) NULL,
    `Genere_id`  INT         NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_Joc_Genere1_idx` (`Genere_id` ASC),
    CONSTRAINT `fk_Joc_Genere1`
        FOREIGN KEY (`Genere_id`)
            REFERENCES `BDJocs`.`Genere` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `BDJocs`.`Puntuacions`
(
    `jugador_id` INT NOT NULL,
    `Joc_id`     INT NOT NULL,
    `puntuacio`  INT NULL,
    PRIMARY KEY (`jugador_id`, `Joc_id`),
    INDEX `fk_jugador_has_Joc_Joc1_idx` (`Joc_id` ASC),
    INDEX `fk_jugador_has_Joc_jugador1_idx` (`jugador_id` ASC),
    CONSTRAINT `fk_jugador_has_Joc_jugador1`
        FOREIGN KEY (`jugador_id`)
            REFERENCES `BDJocs`.`jugador` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_jugador_has_Joc_Joc1`
        FOREIGN KEY (`Joc_id`)
            REFERENCES `BDJocs`.`Joc` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
) ENGINE = InnoDB;

INSERT INTO `BDJocs`.`Genere` (`id`, `nom`, `descripció`)
VALUES ('1',
        'Arcade', 'Es caracteritzen per la jugabilitat simple, repetitiva i
d\'acció ràpida.');
INSERT INTO `BDJocs`.`Genere` (`id`, `nom`, `descripció`)
VALUES ('2',
        'Plataformes', 'Es controla a un personatge que ha d\'avançar per un
escenari evitant obstacles');
INSERT INTO `BDJocs`.`Genere` (`id`, `nom`, `descripció`)
VALUES ('3',
        'Shoot\'em up', 'Jocs de dispars amb perspectiva en dues dimensions,
caracteritzats per l\'ús continu de dispars, la millora de les
armes, l\'avança automàtic i l\'enfrontament amb enemics de gran
tamany al final de cada missió.');
INSERT INTO `BDJocs`.`Genere` (`id`, `nom`, `descripció`)
VALUES ('4',
        'Agilitat mental', 'Posen a prova la intel·ligéncia del jugador per
a la resolució de problemes, bé de caràcter matemàtic, espacial o lò
gic');

