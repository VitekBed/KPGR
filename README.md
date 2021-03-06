# KPGR1 2019
Jedná se o rešení úkolu z Počítačové grafiky, zadané pro zimní semestr 2019/2020.

Pro všechny nakreslené čáry v úlohách 1 a 2 se používá vlastní algoritmus na kreslení čar "DDA line".
## Zadání
### Úloha 1
- [X] Vytvořte program pro kreslení úsečky zadané dvěma libovolnými koncovými body [x1,y1] a [x2,y2].
- [X] Koncové body úsečky zadávejte interaktivně: stisknutím tlačítka myši označte první vrchol, tažením kreslete pružnou čáru a uvolněním vykreslete finální úsečku.
- [X] Implementujte DDA algoritmus fungující pro všechny kvadranty.
Doplňte program tak, aby bylo možné zadat uzavřený n-úhelník, tzn. vrcholy n-úhelníku uložte do odpovídající datové struktury pro použití v dalších úlohách.
- [X] Vytvořte program pro kreslení pravidelného n-úhelníku zadaného třemi body. Prvním kliknutím zadejte střed opsané kružnice, druhým kliknutím bod na obvodu, tj. poloměr opsané kružnice a natočení n-úhelníku, třetím kliknutím určete délku strany, resp. velikost úhlu tvořící výseč jednoho segmentu n-uhelníku.
- [X] Minimální počet vrcholů n-úhelníku omezte na 3.
- [X] V průběhu kreslení n-úhelníku zobrazujte aktuální tvar útvaru.
- [ ] **Bonus:** implementujte libovolný algoritmus vykreslující vyhlazenou (antialiasovanou) úsečku.

### Úloha 2
- [X] Navažte na předchozí úlohu implementující zadávání a vykreslování uzavřeného polygonu (n-úhelníku).
- [X] Implementujte algoritmus semínkového vyplnění rastrově zadané oblasti.
- [X] Myší zadanou hranici oblasti vykreslete na rastrovou plochu plátna barvou odlišnou od barvy vyplnění.
- [X] Kliknutím vyberte počáteční pixel záplavového algoritmu a plochu vybarvěte
- [X] Uvažujte dvě možnosti hraniční podmínky vyplňování. Jednak omezení barvou pozadí a jednak barvou hranice.
- [ ] **Bonus:** Při vyplňovaní rastrově zadané hranice implementujte také variantu vyplnění útvaru pravidelně se opakujícím vzorem zadaným čtvercovou maticí, stačí jako konstanta v kódu.
- [X] Implementujte algoritmus ořezání libovolného uzavřeného n-úhelníku konvexním n-úhelníkem. Oba útvary jsou zadány seznamem úseček tvořících jejich obvod (geometricky zadaná hranice).
- [X] Implementujte Scan-line algoritmus vyplnění plochy n-úhelníku, který je výsledkem ořezání v předchozím kroku.
- [ ] **Bonus:** Doplňte možnost editace již zadaného n-úhelníku, změna pozice vrcholu, případně smazání stávajícího či přidání nového vrcholu. 

### Úloha 3
- [X] Vytvořte program pro transformaci a zobrazení drátového modelu jednoduché grafické prostorové scény složené alespoň ze dvou těles (např. kvádr, jehlan, pravidelný čtyřstěn, …).
- [X] Implementujte transformace posunutí, otočení a změnu měřítka.
- [X] Implementujte perspektivní i paralelní projekci.
- [X] Implementujte pohledovou transformaci.
- [X] Použijte skládání transformací pomocí násobení matic, využijte aktuální verzi knihovny transforms!
- [X] Soustřeďte se na správné ořezávání jednotlivých hran.
- [X] Implementujte interaktivní ovládání myší (rozhlížení) a klávesnicí (WSAD), především u definice kamery (třída Camera v transforms).
- [X] Pro kontrolu projekce je vhodné zobrazovat osy souřadnicového systému scény jako objekt scény (tři úsečky), transformovaný zvolenou projekční a pohledovou maticí.
- [X] Doplňte program o zobrazení zadané parametrické křivky definované v kartézských, sférických nebo cylindrických souřadnicích.
- [X] Doplňte předcházející program pro vykreslení hladkých křivek (Fergusonova, Bézierova a Coonsova kubika) pomocí zadaných čtyř pevných řídících bodů. Použijte definice kubik pomocí matic! Koncové body umístěte ve význačných bodech zvoleného tělesa, například v protilehlých vrcholech krychle. Ostatní body umístěte tak, aby bylo vidět zakřivení křivky v prostoru.
- [ ] **Bonus:** rozšiřte počet řídících bodů na šest. Pokuste se o hladké napojení navazujících křivek. 

## Ovládání
## 3D Controller
Pro úlohu 3 je potřeba v _AppStart_ vyměnit **Controller** za **Controller3D**
### Klávesnice
- **W A S D** - slouží k standardnímu pohybu vpřed, vzad a do stran
- **Q E** - slouží k otáčení doleva a doprava
- **Shift Control** - slouží k pohybu nahoru a dolu
- **I K J L** - alternativně **šipky** slouží k natáčení nahoru, dolu, doleva a doprava
### Myš
- **Levé tlačítko myši** - slouží k natáčení nahoru, dolu, doleva a doprava
- **Pravé tlačítko myši** - slouží k pohybu do stran a nahoru a dolu
- **Kolečko myši** - slouží k pohybu dopředu a dozadu  

## 2D Controller
Pro úlohy 1 a 2 je potřeba v _AppStart_ vyměnit **Controller3D** za **Controller**
#### Úlohy
Ovládíní je připraveno jako volba typu úlohy a jednoduché klikání myší. Nejprve je tedy potřeba zvolit číslo úlohy, následně podle pokynů levým a pravým klikáním myši provádět akce.
- *mezerník* - Vyčištění plátna.
- *0* - Čára ze středu
  - kliknutím nebo tažením se kreslí čára ze středu.
- *1* - Čára mezi body
  - prvním kliknutím myší začnete kreslit čáru, druhým kliknutím kreslení ukončíte.
- *2* - Polygon
  - pravým kliknutím přidáváte body n-úhelníku (úsečky se průběžně vykreslují)
  - levým kliknutím uzavřete polygon, který se celý znovu překreslí.
- *3* - Pravidelný n-úhelník
  - prvním kliknutím udejte střed n-úhelníků
  - druhým kliknutím udejte místo definovaného vrcholu
  - pohybem myši udejte vzdálenost sousedního bodu a kliknutím potvrďte
  - lze nakreslit n-úhelník až o 3 - 36 vrcholech
- *4* - Vyplnění záplavovým algoritmem
  - kliknutím udejte místo ze kterého dojde k vybarvení plochy
- *5* - Ořez polygonu konvexním n-úhelníkem
  - nejprve zvolte 2 a zadejte libovolný polygon
  - poté stiskněte 5 a zadejte konvexní n-úhelník
    - nekonvexní n-úhelník není (nemělo by být) možné zadat
  - ukončení zadávání polygonů je vždy pravým tlačítkem myši
- *6* - Ořez s vyplněním ořízlého útvaru
   - nejprve zvolte 2 a zadejte libovolný polygon
   - poté stiskněte 5 a zadejte konvexní n-úhelník
     - nekonvexní n-úhelník není (nemělo by být) možné zadat
   - ukončení zadávání polygonů je vždy pravým tlačítkem myši

Pokud by jste se ve kteroukoliv chvíli dostali do situace, kdy vám program neumožní pokračovat v zadávání,
stiskněte mezerník a zkuste objekt zadat znovu.

#### Barvy
Po dlouhém zvažování jak umožnit uživateli kreslit jím zvolenou barvou jsem došel k návrhu míchání barev. Stiskem klávesy se zvolí jedna ze základních barev a pomocí + a - se nastaví její hodnota. Zvolená barva a výsledek míchání se zobrazují jako dva malé sousedící čtverečky v pravém horním rohu obrazovky.
- **r** - červená barva
- **g** - zelená barva
- **b** - modrá barva
- **+** - přidání podílu
- **-** - odebrání podílu
***

## Kdo jsem
Jsem Vít Bednář a živím se programováním C#. Nejsem sběhlý ani v návrhu vícevrstvých aplikací, v mém zaměstnání vše co tvořím je na úrovni aplikační vrtvy. Javu nemám zrovna v oblibě i když s dobrým vývojovým prostředím jí začínám trošku přicházet v některých směrech na chuť.
