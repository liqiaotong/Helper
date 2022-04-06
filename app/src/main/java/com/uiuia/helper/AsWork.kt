package com.uiuia.helper

class AsWork {

    var isSolved: Boolean = false
    var isLocked: Boolean = false
    var delay: Long? = null
    var workName: String? = null
    var pageNodes: MutableList<Node>? = null
    var assistAction: AssistAction? = null
    var operationAction: OperationAction? = null
    var packageName: String? = null
    var workType: WorkEnum = WorkEnum.ASSIST

    class Node {
        var id: String? = null
        var text: String? = null
        var type: String? = null
        var parentLevel: Int = 0
        var top: Int = 0
        var left: Int = 0
        var bottom: Int = 0
        var right: Int = 0
    }

    class OperationAction {
        var targetNode: Node? = null
        var action: AssistActionEnum? = null
        var actionContent: String? = null
        var actionX: Int? = 0
        var actionToX: Int? = 0
        var actionY: Int? = 0
        var actionToY: Int? = 0
    }

    class AssistAction {
        var targetNode: Node? = null
        var action: AssistActionEnum? = null
        var actionContent: String? = null
        var actionX: Int? = 0
        var actionToX: Int? = 0
        var actionY: Int? = 0
        var actionToY: Int? = 0
    }

}
