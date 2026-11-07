# Lunarians (Mindustry Mod) — Source Decompiled

**Versão:** 3.78.0  
**Autor original:** gubby  
**Main class:** `lunarians.LunariansMod`  
**Min Game Version:** 154  
**Java mod** (PC + Mobile / classes.dex)

## Descrição
Mod oculto (`hidden: true`) para Mindustry com várias features de automação, UI, schematic panel, auto-fill, free cam, hub/chat global, alertas, formações, etc.

Este repositório contém o **código-fonte decompilado** a partir do JAR oficial `Lunarians-v3_78_0-PC-Mobile.jar`.

> **Aviso:** Código gerado por decompilador (JADX). Pode conter pequenas imprecisões, nomes de variáveis locais genéricos e comentários de decompilação. Serve principalmente para análise, estudo e uso com LLMs (ChatGPT, Claude, Grok, etc.).

## Estrutura

```
Lunarians-src/
├── mod.json
├── assets/
│   └── icon.png
├── src/
│   └── lunarians/
│       ├── LunariansMod.java          # Entrada principal
│       ├── Auto*.java                 # Sistemas de automação
│       ├── FreeCam.java, CamFollow.java, ...
│       ├── data/                      # Slots de configuração
│       ├── hub/                       # Sistema de Hub / Chat global / Presence
│       │   └── HubMod.java            # Segunda entrada de mod
│       └── ui/                        # Interfaces e painéis
└── README.md
```

## Como usar com ChatGPT / Claude / Grok

1. Faça upload desta pasta (ou zip) no chat.
2. Ou cole os arquivos `.java` relevantes.
3. Exemplos de prompts:
   - "Explique como funciona o AutoFill deste mod"
   - "Adicione uma nova feature X baseada no código do Lunarians"
   - "Corrija / melhore o decompile da classe Y"
   - "Gere um build.gradle compatível com Mindustry mod Java"

## Compilação (aproximada)

Mindustry Java mods geralmente usam:

```gradle
// build.gradle (exemplo básico)
plugins {
    id 'java'
}

sourceCompatibility = 17
targetCompatibility = 17

repositories {
    mavenCentral()
    maven { url 'https://www.jitpack.io' }
}

dependencies {
    compileOnly 'com.github.Anuken.Mindustry:core:v154'
    // ou a versão correspondente
}

jar {
    archiveFileName = "Lunarians.jar"
}
```

Depois coloque o `mod.json` e `icon.png` no JAR.

## Features principais (pelo código)

- AutoFill, AutoCargo, AutoDefence, AutoDrill, AutoHeal, AutoMoon, AutoShield, AutoUnit, AutoAttack
- SchematicPanel + SchematicPlacer
- FreeCam / CamFollow
- EnemyCoreAlert, ProductionAlert, ResourceLossWarn
- Formations, EnergyWatch, PathSafe, WeatherSafe
- LunariansHub (chat global, presence, avatars, skins, rooms)
- UI customizada (BetaHud, vários dialogs e painéis)
- Configurações persistentes (LunariansSettings)
- i18n (LunariansI18n)

---

**Gerado automaticamente a partir do JAR em 07/11/2026.**  
Código decompilado com JADX 1.5.1.

**Atualização:** Novo JAR recebido em 07/11/2026 (tamanho 1.168.635 bytes). Aguardando atualização do binário em `dist/`.
