# Lenin's Castle (Forge 1.21.1)

Мод: процедурный замок, босс **Ленин**, приручаемые **вороны**, предметы **Меч короля** и **Вороньи сапоги**.

## Что внутри

| Контент | Описание |
|---------|----------|
| `/leninscastle build` | Строит замок рядом с игроком и спавнит Ленина |
| `/leninscastle build_with_boss` | То же (алиас) |
| `/leninscastle summon_lenin` | Только босс |
| Ворон | Приручается мясом, можно сесть (ПКМ), Shift+ПКМ — ждать |
| Вороньи сапоги | Кожаные сапоги + перо ворона → плавное падение |
| Меч короля | Дроп с Ленина, высокий урон |
| Worldgen | Редкий замок в Overworld (не во всех чанках) |

## Как собрать

1. Установи **JDK 21**.
2. В корне проекта:
   ```bash
   ./gradlew build
   ```
   (Если нет wrapper — скопируй `gradlew` + `gradle/` из [Forge MDK 1.21.1](https://files.minecraftforge.net/).)
3. Готовый jar: `build/libs/LeninsCastle-1.0.0.jar` → папка `mods`.

Или открой папку в IntelliJ IDEA / VS Code с расширением Gradle и запусти конфигурацию `runClient`.

## Структура (восстановленная)

```
src/main/java/ru/leninscastle/
  LeninsCastle.java          — точка входа
  command/CastleCommands.java
  entity/{RavenEntity,LeninBossEntity}.java
  item/{KingsSwordItem,RavenBootsItem}.java
  event/RavenBootsEvents.java
  registry/{ModItems,ModEntities,ModFeatures}.java
  world/{CastleFeature,CastleBuilder}.java
  client/...
```

## Примечание по починке

Исходный zip с GitHub был сильно повреждён:
- файлы с именами вроде `ModFeatures (6).java`, `castle (11).json`;
- содержимое Java-классов лежало в `.json` / `.md` / `.properties`;
- не было нормальной структуры пакетов и `CastleBuilder`.

Всё уникальное содержимое извлечено и собрано в рабочий каркас Forge 1.21.1.
