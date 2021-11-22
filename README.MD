# Вариант 10 Лабораторная 2, этап 1

## Логические элементы
Краткая справка по предметной области: логический элемент – это устройство, которое выполняет логическую функцию (операцию) над входными сигналами (операндами).
Соответственно, логические элементы выполняют следующие действия:
* «AND» - логическое «И»
* «OR» - логическое «ИЛИ»
* «NOT» - логическое «НЕ»
* «XOR» - исключающее «ИЛИ»

Все элементы, кроме «NOT», принимают на вход два сигнала, «NOT» – один. Также все элементы содержат один выход, на который подается результат логической операции.

Программа должна позволять строить логическую схему из множества элементов, которые добавляются соответствующей командой с указанием типа логического элемента. Для того чтобы множество элементов превратилось в полноценную схему, программа должна предоставлять возможность установить связь между входами и выходами элементов, а также установки значений на входы (если к нему не подключен выход другого элемента).

При старте программы выводится небольшая справка по её использованию. Пример сеанса работы:

> Commands:

> add {elemType}: command to add element of {elemType} type. Possible {elemType} values: and, not, xor, or.

>add {in/out} {name}: command to add input or output for element, which added by user on previous step.

>connect {n}-{m}: command to connect {n}’s output and {m}’s input.

>print: command to display output value of the scheme.

>show {n}: command to display information about the logical element: name of this element, names of blocks, which connected with it or value on input(s). {n} is the number of logical element.

> add or 1 created 1:or
>
> add not 2 created 2:not
>
> add xor M created M:xor > add in A created 4:input > add in B created 5:input
>
> add out R created 6:out
>
> connect 4-1[0] > connect 5-1[1] > connect 1-2
>
> connect 2-3[0] > connect 5-3[1] > connect 5-6
>
> set A true
>
> set B false
>
> print
>
>R: False
>
> show 3 
>
>3:xor(2:not, 4:B)