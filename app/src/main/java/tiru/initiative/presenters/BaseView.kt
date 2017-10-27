package tiru.initiative.presenters


interface BaseView<T> {
    fun setPresenter(presenter: T)
}