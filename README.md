# Devilized Lunarians System

Source descompilado (JD-Core) do mod **Lunarians v3.78.0** para Mindustry.

## Instalar (jogadores)
Baixe o arquivo `.jar` (da pagina de **Releases** ou de `dist/`) e importe no Mindustry em **Mods -> Import Mod -> Import File**.
**Nao** importe o ZIP do repositorio ("Download ZIP"): ele so tem codigo-fonte, nao o mod compilado.
O mesmo `.jar` funciona no PC e no Android (contem `.class` e `classes.dex`).

## Estrutura
- `mod.json` - manifesto do mod
- `assets/icon.png` - icone
- `dist/Lunarians-v3_78_0-PC-Mobile_1.jar` - mod compilado (PC + Android)
- `src/lunarians/` - codigo-fonte unico (PC e Android); `LunariansMod` e a classe principal
  - `data/` - slots de configuracao
  - `hub/` - hub, chat global e presenca
  - `ui/` - paineis e dialogs
- `build.gradle`, `settings.gradle`, `gradle.properties`, `gradlew*` - build
- `.github/workflows/build.yml` - GitHub Actions: gera o `.jar` hibrido a cada push

## Compilar
Precisa de JDK 17, Android SDK (`ANDROID_HOME`, com build-tools e uma plataforma) e internet:

    ./gradlew deploy

O resultado fica em `build/libs/lunarians.jar` (classes para PC + `classes.dex` para Android, gerado pelo d8).
Se o Gradle nao achar o Mindustry, ajuste `mindustryVersion` em `gradle.properties`.

> O codigo-fonte e **descompilado** (JD-Core) do JAR: nomes de variaveis locais e lambdas podem diferir do original e pode precisar de ajustes para compilar.
