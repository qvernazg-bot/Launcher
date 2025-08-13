# MonLauncher — Skeleton (paging + WOW)

Un launcher Android en **Jetpack Compose** avec **menu circulaire** à droite, **pages** (10 apps/page) et **animation WOW** (ripple + effet ressort).

## Ouvrir le projet
1. Android Studio ➜ *Open* ➜ sélectionne `MonLauncher`.
2. Laisse Gradle se synchroniser.
3. Run sur ton appareil, puis appuie sur **Home** pour définir **MonLauncher** comme écran d’accueil.

## Réglages
- Épingle/désépingle des apps dans l’écran **Réglages** (DataStore).
- Le dock montre les épinglées dans l’ordre choisi, sinon les premières 10/20/etc.

## Personnaliser
- Taille de page : `pageSize` dans `RadialDock(...)` (par défaut 10).
- Arc : `arcDegrees` et `radius`.
- Animation : regarde `Animatable` + `spring` et `rippleProgress` (Canvas).

## Versions
- `compileSdk=34`, `minSdk=26`, Kotlin 1.9.x, AGP 8.5.2, Compose BOM 2024.10.01.
