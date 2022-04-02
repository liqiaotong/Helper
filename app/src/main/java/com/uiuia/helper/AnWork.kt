package com.uiuia.helper

class AnWork {

    var verifyPageNodes: MutableList<Node>? = null
    var action: Action? = null
    var delay: Long? = null
    var isSolved: Boolean = false

    class Node {
        var id: String? = null
        var text: String? = null
        var type: String? = null
        var top: Int = 0
        var left: Int = 0
        var bottom: Int = 0
        var right: Int = 0
    }

    class Action {
        var node: Node? = null
        var action: Int? = null
        var actionContent: String? = null
        var actionX: Int? = 0
        var actionToX: Int? = 0
        var actionY: Int? = 0
        var actionToY: Int? = 0

    }

}
