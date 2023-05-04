# SafeLines
<b> Praca Inżynierska </b>

Aplikacja mobilna, która informuje komunikatem uzytkownika o zblizaniu sie do przejscia dla pieszych w celu bezpieczeństwa, gdy np. slucha muzyki lub jest czymś rozproszony.

Aplikacja daje możliwość utworzenia konta dla użytkownika, resetu hasła do konta oraz zalogowania sie do pelnej wersji aplikacji. 

Zalogowany użytkownik ma dostęp do pełnej wersji aplikacji. Po kliknieciu odpowiedniego przycisku ma dostęp do przejść dla pieszych w których może dodawać nowe punkty odpowiadające za przejscia dla pieszych. 

Aplikacja została skonstruowana tak aby lokalizacja telefonu byla porownywana z lokalizacja przejscia dla pieszych, którą to wpisujemy do aplikacji.

Zalogowany uzytkownik po kliknieciu w przycisk włącz ochronę, funkcja która obsluguje porownywanie 2 lokalizacji dziala w tle więc, moze rownie dobrze przegladac internet czy odpisywac na wiadomości lub robic inne rzeczy.

Do poprawnego działania aplikacji musimy mieć dostęp do internetu czy to przez WiFi czy to przez transfer danych oraz do danych lokalizacyjnych. Bez tych 2 rzeczy aplikacja nam nie zadziała. Aplikacja informuje uzytkownika o zbliżającym się przejsciu dla pieszych wibracją oraz sygnałem dzwiękowym o treśći "Uwaga pasy".

Aplikacja zostala zrobiona w srodowisku Android Studio w języku Java na wersję android 8.0+.

Jako baza danych zostala użyta baza NoSQL Firebase w celu przechowywania danych o uzytkownikach oraz punktach oznaczających przejscia dla pieszych [długość i szerokość geograficzna].
