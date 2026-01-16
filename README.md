# API Territoires

<a name="top"></a>

1. [Présentation](#presentation)
2. [Stack technique](#stack-technique)
5. [Contrats d'interface territories](#contrats-d-interface-territories)
6. [Service bookings](#bookingsService)
  * [Service POST /resources/v1/bookingsService](#bookingsServicePost)
  * [Service DELETE /resources/v1/bookingsService/{id}](#bookingsServiceDelete)
  * [Service GET /resources/v1/bookingsService/{id}](#bookingsServiceGet)
  * [Service PUT /resources/v1/bookingsService/{id}](#bookingsServicePut)
7. [Contrats d'interface edition](#contrats-d-interface-edition)
8. [Service POST /services/v1/formatRefund](#formatRefund)


## Présentation <a name="presentation"></a>

L'API territories expose les services REST permettant de gérer les données des territoires (attributions en pdf, etc.).

## Stack technique <a name="stack-technique"></a>

- JDK 12
- Spring Boot


## Contrats d'interface territories <a name="contrats-d-interface-territories"></a>

Le contrat d'interface est disponible [ici](contracts/services_territory.openapi.yaml)


### Services bookings <a name="bookingsService"></a>

### POST /v1/resources/bookings <a name="bookingsServicePost"></a>

Ce service permet de créer une attribution.

![diagramme_sequence](doc/wsResourceBookingsPost.png)

### DELETE /v1/resources/bookings/{id} <a name="bookingsServiceDelete"></a>

Ce service permet de supprimer une attribution.

![diagramme_sequence](doc/wsResourceBookingsDelete.png)

### GET /v1/resources/bookings/{id} <a name="bookingsServiceGet"></a>

Ce service permet de récupérer une attribution.

![diagramme_sequence](doc/wsResourceBookingsGet.png)

### PUT /v1/resources/bookings/{id} <a name="bookingsServicePut"></a>

Ce service permet de modifier une attribution.

![diagramme_sequence](doc/wsResourceBookingsPut.png)


## Exemple de Payload pour le service formatTerritory

```json
{
  "requestId": 181633,
  "countryCode": "FR",
  "languageCode": "fr"
}
```

### POST /services/v1/formatTerritory <a name="formatTerritory"></a>

Ce service génère un territoire
![diagramme_sequence](doc/wsServiceFormatTerritory.png)


## Base de données <a name="base-de-données"></a>

Les informations de l'api sont récupérées de la base de données territories

### Schéma cible : territories

**Tables** :

- address *(table qui contient les informations des adresses)*
- TODO

## Tests du projet <a name="tests"></a>

Pour tester l'edition du territoire :

Utiliser postman avec les urls suivantes : 

```
{{environment}} : -dev, -rec, -pp ou vide pour la prod
{{baseUrl}} : https://api-territories.app{{environment}}.xm

POST {{baseUrl}}/services/v1/formatTerritory
```

ou utiliser swagger avec les contrats sur gitlab directement à ces adresses : 

[services-api-territories-edition.openapi.yaml](https://github.com/sam102022/api-territory/services-api-territories-edition.openapi.yaml)


## Résumé

Les méthodes disponibles sont : 

 - **POST /resources/v1/bookings** : <br />
 	description : Permet de générer un pdf<br />
  	body : données au format JSON<br />
<br />
 	Paramètres requête disponibles : <br />
 	- format : Format de sortie<br />
<br />
 	*Ex : /resources/v1/bookings?format=pdf*<br />
<br />
 - **POST /resources/v1/frequencyBookings** : <br />
 	description : Permet de générer un pdf<br />
  	body : données au format JSON<br />
<br />
 	Paramètres requête disponibles : <br />
 	- groupByFrequency : Regroupement par fréquence de parcours<br />
 	- chart : Affichage d'un graphique par villes<br />
 	- format : Format de sortie<br />
<br />
 	*Ex : /resources/v1/bookings?format=pdf*<br />
<br />
 - **POST /resources/v1/territory** : <br />
 	description : Permet de générer un pdf<br />
  	body : données au format JSON<br />
<br />
 	Paramètres requête disponibles : <br />
 	- format : Format de sortie<br />
<br />
 	*Ex : /resources/v1/territory?format=pdf*<br />
<br />

