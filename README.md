# Devilized Lunarians System

Source descompilado (JD-Core) do mod **Lunarians v3.78.0** para Mindustry.

## Estrutura
- `mod.json` – manifesto do mod
- `assets/icon.png` – ícone
- `dist/Lunarians-v3_78_0-PC-Mobile_1.jar` – mod compilado (o que o Mindustry importa)
- `src/lunarians/` – classes do mod (versão PC, descompilada dos `.class` do JAR) (`LunariansMod` é a principal)
  - `data/` – slots de configuração
  - `hub/` – hub, chat global e presença
  - `ui/` – painéis e dialogs
- `src-mobile/lunarians/` – o mesmo mod descompilado do `classes.dex` (versão Android/mobile; dex → jar com Enjarify, depois JD-Core). Mesma estrutura, 94 classes de nível superior.

> Código descompilado: nomes de variáveis locais e lambdas podem diferir do original e pode precisar de ajustes para recompilar.
