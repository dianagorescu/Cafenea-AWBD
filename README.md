Baza de date: <http://localhost:8080/h2-console> \
Swagger: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html%23/)

# Business requirements

1.  In calitate de **client**, doresc sa pot avea o experienta personalizata, prin inregistrarea cu nume, email si numar de telefon.

2.  Ca **personal al cafenelei**, doresc sa actualizez in timp real statusul unui produs al meniului.

3.  Ca **personal al cafenelei**, doresc sa consult numarul de mese disponibile, respectiv numarul de scaune per masa.

4.  In calitate de **client**, doresc sa pot crea rezervari tinand cont de data si numarul de persoane.

5.  In calitate de **client**, doresc sa pot vedea produsele si suma totala de plata de pe bon.

6.  Ca **admin**, doresc sa existe un sistem de validare al datelor pentru a evita posibile erori.

7.  Ca **admin**, doresc un sistem simplu si usor de folosit si integrat cu interfata UI .

8.  Ca **personal al cafenelei**, doresc sa adaug/ sterg produse din meniu.

9.  In calitate de **client**, doresc sa pot vedea pret, detalii si statusul produselor din meniu.

10. Ca **personal al cafenelei**, doresc sa adaug/ sterg produse din comanda unui client.

## 5 main features -MVP {#main-features--mvp}

### Operatii CRUD asupra meniului:

- \`POST /api/menu-items\` - adauga un produs

- \`GET /api/menu-items\` - listeaza toate produsele

- \`GET /api/menu-items/{id}\` - listeaza produsul cu id-ul -

- \`GET /api/menu-items/available\` - listeaza doar produsele disponibile

- \`PUT /api/menu-items/{id}\` - face update la produsul cu id-ul -

- \`DELETE /api/menu-items/{id}\` - sterge produsul cu id-ul -

  1.  Clientii se pot loga pentru a face rezervari si comenzi pentru urmarirea lor si personalizarea serviciului.

- \`POST /api/customers\` - creare cont

- \`GET /api/customers/{id}\` - listare client dupa id

- \`GET /api/customers\` - listare toti clientii

- \`PUT /api/customers/{id}\` - face update la clientul cu id-ul -

  2.  Permiterea clientiilor de a face rezervari pentru data si numar de persoane specific.(asignare automata). In cazul in care nu se poate efectua o rezervare(nu exista masa), se afiseaza un mesaj corespunzator.

- \`POST /api/reservations\` - creeaza rezervare

- \`GET /api/reservations/{id}\` - listeaza rezervarea cu id-ul -

- \`GET /api/reservations\` - listeaza toate rezervarile

- \`PUT /api/reservations/{id}/complete\` - staff-ul poate marca o rezervare ca "completed"

- \`PUT /api/reservations/{id}/cancel\` - staff-ul poate anula o rezervare

- \`GET /api/reservations/customer/{customerId}\` - listeaza rezervarea asignata clientului cu id-ul -

  3.  Se poate procesa si urmari o comanda (un bon) cu mai multe produse.

- \`POST /api/orders\` - creeaza comanda cu diferite produse

- \`GET /api/orders/{id}\` - listeaza intreaga comanda cu id-ul -

- \`GET /api/orders\` - listeaza toate comenziile

- \`PATCH /api/orders/{id}/status\` - face update la statusul comenzii cu id-ul -

- \`GET /api/orders/customers/{customerId}\` - listeaza toate comenziile clientului cu id-ul -

- \`DELETE /api/orders/{orderId}/items/{orderItemId}\` - sterge produsul cu id-ul - din comanda cu id-ul -

  4.  Staff-ul poate vizualiza cate mese si cate locuri sunt disponibile.

- \`GET /api/tables\` - listeaza toate mesele

- \`GET /api/tables/available\` - listeaza doar mesele disponibile

# ERD

-initial
<img width="507" height="219" alt="image" src="https://github.com/user-attachments/assets/0a5bbf0c-5c7e-40f2-a2a5-959c6386d7cc" />

-final
<img width="579" height="163" alt="image" src="https://github.com/user-attachments/assets/33abe226-fda6-49ab-93da-2991b2c4407f" />

