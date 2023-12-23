package com.example.sudoku

class sudoku {
    var grid = emptyArray<Array<Int>>()

    fun copyArray (a: Array<Array<Int>>): Array<Array<Int>>{
        /** Copy array **/
        var lst1 = emptyArray<Array<Int>>()
        for (i in 0..8){
            var lst = Array<Int>(9, {0})
            for (j in 0..8){
                lst[j] = a[i][j]
            }
            lst1 = lst1.plusElement(lst)
        }
        return lst1
    }

    fun getRow (a:Array<Array<Int>>,  pos: Pair<Int, Int>): Array<Int> {
        /** Get numbers in row for pos **/
        return a[pos.first]
    }

    fun getCol (a:Array<Array<Int>>, pos: Pair<Int, Int>): Array<Int> {
        /** Get numbers in col for pos **/
        var column = emptyArray<Int>()
        for (i in 0..8) {
            column = column.plusElement(a[i][pos.second])
        }
        return column
    }

    fun getBlock (a:Array<Array<Int>>, pos: Pair<Int, Int>): Array<Int> {
        /** Get numbers in block for pos **/
        val row = pos.first / 3 * 3
        val col = pos.second / 3 * 3
        var lst = emptyArray<Int>()

        for (i in row..(row+2)){
            for (j in col..(col+2)){
                lst = lst.plusElement(a[i][j])
            }
        }
        return lst
    }

    fun findEmptyPosition(a:Array<Array<Int>>): Pair<Int, Int> {
        /** Find first empty pos in sudoku **/
        for (i in 0..8){
            for (j in 0..8){
                if (a[i][j] == 0){
                    return Pair(i, j)
                }
            }
        }
        return Pair(-1, -1)
    }

    fun findPossibleValues(a:Array<Array<Int>>, pos: Pair<Int, Int>): Set<Int> {
        /** Find possible values to pos **/
        val rowVal = this.getRow(a, pos)
        val colVal = this.getCol(a, pos)
        val blockVal = this.getBlock(a, pos)

        return setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0) subtract (rowVal + colVal + blockVal).toSet()
    }

    fun checkSolution(a: Array<Array<Int>>) : Boolean{
        /** Return true if solution is right, else false **/
        for (i in a){
            if (i.contains(0)){
                return false
            }
            if (i.size != i.toSet().size){
                return false
            }
        }
        for (i in 0..8 ){
            val s = getCol(a, Pair(0, i))
            if (s.toSet().size != s.size){
                return false
            }
        }

        for (i in 0..8 step 3){
            var s = getBlock(a, Pair(0, i))
            if (s.toSet().size != s.size){
                return false
            }
            s = getBlock(a, Pair(3, i))
            if (s.toSet().size != s.size){
                return false
            }
            s = getBlock(a, Pair(6, i))
            if (s.toSet().size != s.size){
                return false
            }
        }
        return true

    }

    fun transposing(a: Array<Array<Int>>): Array<Array<Int>> {
        /** Transpose grid **/
        var lst1 = emptyArray<Array<Int>>()
        for (i in 0..8){
            var lst = Array<Int>(9, {0})
            for (j in 0..8){
                lst[j] = a[j][i]
            }
            lst1 = lst1.plusElement(lst)
        }
        return lst1

    }

    fun swapRow(A: Array<Array<Int>>): Array<Array<Int>> {
        /** Swap two rows in one area **/
        val area = listOf(0, 3, 6).random()
        val (a, b) = listOf(Pair(0, 1), Pair(0, 2), Pair(1, 2)).random()

        (A[area + a] to A[area + b]).also {
            A[area + b] = it.first
            A[area + a] = it.second
        }
        return A
    }

    fun swapRows(A: Array<Array<Int>>): Array<Array<Int>> {
        /** Swap two areas horizontally **/
        val (a, b) = listOf(Pair(0, 3), Pair(0, 6), Pair(3, 6)).random()
        for (i in 0..2){
            (A[i + a] to A[i + b]).also {
                A[i + b] = it.first
                A[i + a] = it.second
            }
        }
        return A
    }

    fun generateSudoku(n: Int): Array<Array<Int>> {
        /** Generate sudoku **/
        var a = emptyArray<Array<Int>>()
        a = a.plusElement(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9).toTypedArray())
        for (i in 0..1){
            a = a.plusElement((a.last().takeLast(3) + a.last().take(6)).toTypedArray())
        }

        for (i in 0..1){
            a = a.plusElement((a[a.size-3].takeLast(8) + a[a.size-3][0]).toTypedArray())
            a = a.plusElement((a[a.size-3].takeLast(8) + a[a.size-3][0]).toTypedArray())
            a = a.plusElement((a[a.size-3].takeLast(8) + a[a.size-3][0]).toTypedArray())
        }

        for (i in 1..10){
            var f = listOf(0, 1, 2).random()
            a = when (f){
                0 -> this.swapRow(a)
                1 -> this.swapRows(a)
                else -> this.transposing(a)

            }
        }


        var n = Math.max(81-n, 0)
        while (n != 0){
            var c = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8).random()
            var b = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8).random()
            if (a[c][b] != 0){
                a[c][b] = 0
                n--
            }
        }
        return a
    }

    fun printGrid(A: Array<Array<Int>>) {
        /** Print grid **/
        for (i in A){
            for (j in i){
                print("$j ")
            }
            println()
        }
    }

    fun solve(a: Array<Array<Int>>): Array<Array<Int>> {
        /** Solve function **/
        val empty_pos = this.findEmptyPosition(a)
        if (empty_pos.first == -1 && empty_pos.second == -1){
            return a
        }

        val values = findPossibleValues(a, empty_pos)
        for (i in values){
            val grid1 = copyArray(a)
            grid1[empty_pos.first][empty_pos.second] = i
            val slv = solve(grid1)
            if ((slv.size != 0) && this.findEmptyPosition(slv) == Pair(-1, -1)) {
                return slv
            }
        }
        return emptyArray<Array<Int>>()

    }
}