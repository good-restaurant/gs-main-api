# gs-nonvector-api
Project for non-vector related api using with RESTful communication.

## .env setting

.env is `.gitignored` and only accepted in local 

- DB_URL=jdbc:postgresql:`{DATABASE_URL}`
- DB_USERNAME=`UserName`
- DB_PASSWORD=`PassWord`

## Network Flow

* only Deploy Operation

```mermaid
sequenceDiagram
    participant Internet
    participant nginx_443 as nginx 443
    participant nginx_993 as nginx 993
    participant bridge as docker bridge network
    participant app_8080 as app 8080 tomcat http server
    Internet ->> nginx_443: HTTPS Request
    nginx_443 ->> nginx_993: SSL Termination
    nginx_993 ->> bridge: HTTP Forward
    bridge ->> app_8080: HTTP Request
    app_8080 -->> bridge: HTTP Response
    bridge -->> nginx_443: HTTP Response
    nginx_443 -->> Internet: HTTPS Response
```
