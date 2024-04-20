package io.github.pandier.intellijdiscordrp.settings.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.ClearableLazyValue
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.NlsContexts
import javax.swing.JComponent

abstract class DslConfigurable(
    @NlsContexts.ConfigurableName private val displayName: String,
) : Configurable {
    protected var disposable: Disposable? = null
        private set

    private val panel = object : ClearableLazyValue<DialogPanel>() {
        override fun compute(): DialogPanel {
            if (disposable == null)
                disposable = Disposer.newDisposable()
            val panel = createPanel()
            panel.registerValidators(disposable!!)
            return panel
        }
    }

    abstract fun createPanel(): DialogPanel

    override fun getDisplayName(): String =
        displayName

    final override fun createComponent(): JComponent =
        panel.value

    override fun isModified() =
        panel.value.isModified()

    override fun reset() {
        panel.value.reset()
    }

    fun validateAndApply(): Boolean =
        panel.value.validateAll().isEmpty().also { if (it) panel.value.apply() }

    override fun apply() {
        validateAndApply()
    }

    override fun getPreferredFocusedComponent(): JComponent? =
        panel.value.preferredFocusedComponent

    override fun disposeUIResources() {
        disposable?.let {
            Disposer.dispose(it)
            disposable = null
        }

        panel.drop()
    }
}