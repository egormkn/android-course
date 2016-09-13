# Покемонный калькулятор

В рамках этого практического сеанса мы напишем простое приложение, которое вычисляет скрытые характеристики покемонов из популярной игры Pokemon Go, и научимся делаться верстку, работать с ресурсами, обрабатыватьс клики и другие события интерфейса.

## Введение

Летом 2016 года компания Niantic выпустила на Android и iOS быстро ставшую популярной игру с дополненной реальностью Pokemon Go. Одним из элементов игры является прокачка пойманных покемонов с целью увеличения их боевых характеристик: CP (Combat Points) и HP (Health Points). Интерфейс игры показывает текущее значение CP и HP, а также стоимость апргейда (стоимость апгрейда часто назывыют словом Stardust -- по названию игрового ресурса, который тратится на апгрейд). Пользователь жмет на кнопку "Power up", и в результате покемон прокачивается -- тратится указанное количество Stardust, а CP и HP увеличиваются. 

<img src="https://github.com/dtrounine/pokecalc/blob/master/screenshots/pokemon_go/slowpoke.png" width=360px/>

Проблема заключается в том, что CP и HP являются функцией скрытых параметров (IV, Internal Values) покемона и его уровня, которые в интерфейсе игры нигде не отображаются, и игрок не может их никак узнать. Зависимость CP и HP от скрытых параметров такова, что результат прокачки покемона может сильно от них зависить, и иногда можно достичь лучшего результата (больших значений CP и HP), потратив меньше Stardust, если прокачивать изначально более слабого покемона, имеющего сильные скрытые параметры, чем если прокачивать изначально более сильного покемона, но со слабыми скрытыми параметрами. Поэтому многие игроки в Pokemon Go озадачились вопросом: как узнать скрытые параметры покемонов? В результате в магазинах приложений Google Play и App Store появилось огромное количество приложений, помогающих вычислить эти скрытые параметры. Эти приложения можно найти по словам pokemon iv calculator, pokemon info, pokemon tools и т.п. -- их там уже десятки. А мы сегодня напишем еще одно подобное простое приложение-калькулятор.

## Устройство калькулятора

Приложение состоит из одного экрана, на котором пользователь сначала выбирает тип покемона в списке из 150 вариантов, потом вводит значения CP, HP и Stardust, которые он видит в интерфейсе игры Pokemon Go, нажимает на кнопку "Вычислить" -- и приложение проводит необходимые вычисления и выводит на экран возможные значения скрытых параметров и уровня покемона либо сообщение об ошибке, если введенные данные не могут быть обработаны.

### Скрытые параметры

Каждый покемон обладает тремя врожденными характеристикама: *attack*, *defense* и *stamina* -- они никогда не меняются. Каждая из этих характеристик складывается из базового значения, общего для всех покемонов одного типа, и скрытого значнеия (того самого internal value):

```
attack = base_attack + iv_attack
defense = base_defense + iv_defense
stamina = base_stamina + iv_stamina
```

Каждый из скрытых параметров -- *iv_attack*, *iv_defense*, *iv_stamina* -- имеет возможное значение от 0 до 15. У каждого экземпляра покемона эти значения свои собственные, он получает их от рождения, и они никогда не меняются. Лучшая комбинация -- это (15, 15, 15). Худшая -- (0, 0, 0). Основная задача приложения заключается в том, чтобы вычислить эти три значения.

### Уровень

Уровень может иметь значение от 2 до 80 и увеличивается на 1 каждый раз, когда покемон прокачивается. Пойманный покемон имеет какой-то случайный уровень. С уровнем тесно связана стоимость апгрейда (Stardust):

Level | Stardust
----- | --------
2 - 5 | 200
6 - 9 | 400
10 - 13 | 600
14 - 17 | 800
18 - 21 | 1000
22 - 25 | 1300
26 - 29 | 1600
30 - 33 | 1900
34 - 37 | 2200
38 - 41 | 2500
42 - 45 | 3000
46 - 49 | 3500
50 - 53 | 4000
54 - 57 | 4500
58 - 61 | 5000
62 - 65 | 6000
66 - 69 | 7000
70 - 73 | 8000
74 - 77 | 9000
78 - 80 | 10000

Таким образом, зная Stardust, можно определить диапазон из четырех уровней покемона, скрытые параметры которого надо вычислить. Также видно, что Stardust может принимать только строго определенные значения, и не все значения, которые может ввести пользователь, правильные.

### Формулы CP и HP

CP и HP зависят от четырех параметров: attack, defense, stamina и level:

```
level_factor = if (level <= 20) {
    0.009426125 * level - 0.010016255
} else if (level <= 60) {
    0.0089219657 * level + 0.0000389325
} else {
    0.004459461 * level + 0.267817222
}

CP = attack * SQRT(defense) * SQRT(stamina) * level_factor / 10

HP = stamina * SQRT(level_factor)
```

Вычисленные по этим формулам значения CP и HP округляются *вниз* до ближайшего целого (Math.floor).

### Вычисление скрытых параметров

Задача калькулятора -- по введенным пользователем значениям Stardust, CP и HP и типу покемона определить возможные значения level, iv_attack, iv_defense и iv_stamina. Вычисления можно делать по следующему приблизительному алгоритму:

1. По значению Stardust определить интервал возможных значений level
2. Из таблиц базовых характеристик по типу покемона взять значения base_attack, base_defense, base_stamina
3. Из всех возможных комбинаций значений level (из интервала, полученного на шаге 1), iv_attack (от 0 до 15), iv_defense (от 0 до 15) и iv_stamina (от 0 до 15) выбрать те, которые при вычислении CP и HP по формулам, указанным выше, дают значения, введенные пользователем.

Базовые значения attack, defense и stamina определены в ресурсах приложения: [pokemon_data.xml](https://github.com/dtrounine/pokecalc/blob/master/app/src/main/res/values/pokemon_data.xml). Также их можно найти в интернете -- например, [здесь](http://www.pokemongodb.net/2016/07/pokemon-by-attack.html).

<details>
  <summary>Пример</summary>

Для пример вычислим скрытые параметры для слоупока со скриншота. 

Входные параметры:
```
CP = 449
HP = 94
Stardust = 1900
```

По значению Stardust=1900 определяем возможные значения level: 
```
level: 30, 31, 32, 33
```
Берем базовые значения для слоупока: 
```
base_attack = 110 
base_defense = 110
base_stamina = 180
```
Перебираем все возможные значения level, iv_attack, iv_defense, iv_stamina:
```
1) (level, iv_attack, iv_defense, iv_stamina) = (30, 0, 0, 0)

attack = base_attack + iv_attack = 110 + 0 = 110
defense = base_defense + iv_defense = 110 + 0 = 110
stamina = base_stamina + iv_stamina = 180 + 0 = 180
level_factor(30) = 0.0089219657 * 30 + 0.0000389325 = 0.2676979
CP = attack * SQRT(defense) * SQRT(stamina) * level_factor / 10
   = 110 * SQRT(110) * SQRT(180) * 0.2676979 / 10
   = 414.3528
   ~ 414
HP = stamina * SQRT(level_factor) = 180 * SQRT(0.2676979) = 93.13115
   ~ 93
   
Вычисленные значения CP=414 и HP=93 не совпадают с введенные пользователем CP=449 и HP=94, поэтому данные набор значений (30, 0, 0, 0) не включаем в ответ.
```
Продолжаем перебирать...
```
1843) (level, iv_attack, iv_defense, iv_stamina) = (30, 7, 3, 2)

attack = base_attack + iv_attack = 110 + 7 = 117
defense = base_defense + iv_defense = 110 + 3 = 113
stamina = base_stamina + iv_stamina = 180 + 2 = 182
level_factor(30) = 0.0089219657 * 30 + 3.89325e-05 = 0.2676979035
CP = attack * SQRT(defense) * SQRT(stamina) * level_factor / 10
   = 117 * SQRT(113) * SQRT(182) * 0.2676979035 / 10
   = 449.164834325939
   ~ 449
HP = stamina * SQRT(level_factor) = 182 * SQRT(0.2676979035) = 94.1659458378346
   ~ 94
   
Эти значения CP=449 и HP=94 совпадают со значениями, введенными пользователем -- включаем этот вариант (30, 7, 3, 2) в ответ.
```

В итоге после полного перебора 4*15*15*15 = 13500 вариантов, подходящими оказываются 12:

```
(30, 1, 15, 3)
(30, 2, 13, 3)
(30, 4, 9, 2)
(30, 5, 7, 2)
(30, 6, 5, 2)
(30, 7, 3, 2)
(31, 0, 11, 0)
(31, 1, 9, 0)
(31, 2, 7, 0)
(31, 3, 5, 0)
(31, 4, 3, 0)
(31, 5, 1, 0)
```
</details>

## Задание

### 1 Верстка

Создать верстку основного экрана приложения, которому соответствует класс PokeCalcActivity. Для этого создайте XML файл в папке layout и используйте его для инициализации экрана в методе PokeCalcActivity.onCreate(). На экране должны быть следующие элементы:

* Выпадающий список с названиями покемонов (использовать [Spinner](https://developer.android.com/guide/topics/ui/controls/spinner.html))
* Изображение покемона ([ImageView](https://developer.android.com/reference/android/widget/ImageView.html))
* Поля ввода значений Stardust, CP, HP ([EditText](https://developer.android.com/guide/topics/ui/controls/text.html))
* Описание полей ([TextView](https://developer.android.com/reference/android/widget/TextView.html))
* Кнопка, запускающая вычисление ([Button](https://developer.android.com/reference/android/widget/Button.html))

Для организации верстки могут понадобиться классы LinearLayout, RelativeLayout, FrameLayout, асли все необходимые элементы не будут помещаться на экране, то может понадобиться ScrollView.

Один из возомжных вариантов UI, который может получиться:

<img src="https://github.com/dtrounine/pokecalc/blob/master/screenshots/poke_calc_layout.png" width="360px"/>

### 2 Обработка выбора покемона

Реализовать выбор покемона из выпадающего списка и отображение соответствующей ему картинки

Выпадающий список (Spinner) должен содержать названия покемонов, которые определены в ресурсах в массиве R.arrray.pokemon_names (Или @array/pokemon_names в XML). Обработчик Spinner.OnItemSelectedListener должен загрузить изображение выбранного покемона из ресурсов. Для этого в ресурсах определен массив R.array.pokemon_images -- он содержит ID картинок для каждого возможного выбора. 

Код для получения ID картинки может выглядеть приблизительно так:
```Java
int[] pokemonImageIds = getResources().obtainTypedArray(R.array.pokemon_images);
int imageResId = pokemonImageIds[position]; // position берется из обработчика Spinner.OnItemSelectedListener
```
О доступе к ресурсам можно почитать [здесь](https://developer.android.com/guide/topics/resources/accessing-resources.html)

### 3 Обработка клика и вычисление

* Сделать обработчик клика на кнопку "Вычислить"
* По клику на кнопку должно запускаться вычисление скрытых параметров, используя в качестве входных данных значения, введенные в поля ввода Stardust, CP и HP, а также тип выбранного покемона
* Базовые значения для каждого типа покемонов определены в ресурсах в массивах R.array.pokemon_stamina, R.array.pokemon_attack и R.array.pokemon_defense
* Результат вычислений отобразить в отдельном текстовом поле (TextView)

Некоторые вспомогательные функции, которые могут пригодиться для вычислений, определены в классе PokeMath. Например, там есть функция для вычисления level_factor, CP и HP. 

Код для получения базовых значений может выглядеть так:
```Java
// position выбранного варианта берется из Spinner-а
int baseStamina = getResources().getIntArray(R.array.pokemon_stamina)[position];
int baseAttack = getResources().getIntArray(R.array.pokemon_attack)[position];
int baseDefense = getResources().getIntArray(R.array.pokemon_defense)[position];
```

Результат может выглядеть приблизительно так:

<img src="https://github.com/dtrounine/pokecalc/blob/master/screenshots/output_layout.png" width="360px"/>

Для тестирования и отладки можно использовать следующие варианты входных данных (либо брать прямо из игры Pokemon Go):

<details>
  <summary>Данные</summary>
  
Pokemon | Stardust | Cp | Hp
--------|----------|----|----
Abra | 800 | 104 | 20
Bellsprout | 1000 | 268 | 42
Clefairy | 1600 | 378 | 72
Cubone | 2200 | 499 | 64
Drowzee | 2500 | 577 | 75
Eevee | 800 | 215 | 41
Exeggcute | 1900 | 462 | 67
Horsea | 1300 | 248 | 31
Magikarp | 2500 | 106 | 23
Nidorino | 400 | 90 | 28
Pikachu | 1900 | 360 | 41
Psyduck | 2200 | 494 | 64
Psyduck | 1600 | 382 | 55
Psyduck | 1600 | 350 | 54
Psyduck | 1300 | 330 | 48
</details>


### 4 Восстановление состояния

Как мы знаем, в Android активность может быть уничтожена и создана снова почти в любой момент времени по многим разным причинам. Простейший случай -- это поворот экрана. Когда поворочивается экран (или вообще происходит событие типа configuration change), активность со всеми View уничтожается и создается снова с нуля. В нашем приложение это может иметь такой эффект: если до поворота экрана пользователь уже нажал "Вычислить", и на экране был отображен результат, то после поворота этот результат исчезает, потому что вся верстка была создана с нуля. Это не нормально, потому что при повороте должно сохраняться состояние экрана. Подробнее о сохранении и восстановлении состояния можно прочитать здесь: [https://developer.android.com/guide/topics/resources/runtime-changes.html](https://developer.android.com/guide/topics/resources/runtime-changes.html)

Задание: сделать сохранение состояние экрана (результат вычислений) и восстановление его после поворота. 

### 5 Поддержка разных конфигураций экрана

Создайте альтернативный вариант верстки для экрана в ландшафтной ориентации. Для этого создайте папку layout-land, а в ней файл верстки с тем же названием, что и в дефолтной конфигурации. Верстка должна содержать те же основные элементы с теми же ID, но располагаться они могут по-другому. Возможный вариант верстки может выглядеть так:

<img src="https://github.com/dtrounine/pokecalc/blob/master/screenshots/poke_calc_layout_land.png" width="720px"/>

