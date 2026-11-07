# Devilized Lunarians System

Source descompilado (JD-Core) do mod **Lunarians v3.78.0** para Mindustry.

## Estrutura
- `mod.json` – manifesto do mod
- `assets/icon.png` – ícone
- `src/lunarians/` – classes do mod (versão PC, descompilada dos `.class` do JAR) (`LunariansMod` é a principal)
  - `data/` – slots de configuração
  - `hub/` – hub, chat global e presença
  - `ui/` – painéis e dialogs

- `src-mobile/lunarians/` – o mesmo mod descompilado do `classes.dex` (versão Android/mobile; dex → jar com Enjarify, depois JD-Core). Mais verboso que o PC, com 94 classes de nível superior iguais.

> Código descompilado: nomes de variáveis locais e lambdas podem diferir do original e pode precisar de ajustes para recompilar.
