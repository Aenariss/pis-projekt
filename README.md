# pis-projekt

## Závislosti

- java 17 (např. openjdk-17-jdk),
- maven

## Spuštění

```bash
mvn process-resources # stáhne závislosti frontendu a sestaví ho
mvn liberty:dev # spustí vývojový server
```

Poté je stránka dostupná na adrese <http://localhost:9089/>.
