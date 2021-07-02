/*
 * Copyright (c) 2017-present Robert Jaros
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.kvision.form.upload

import com.github.snabbdom.VNode
import io.kvision.KVManagerUpload
import io.kvision.core.AttributeSetBuilder
import io.kvision.core.ClassSetBuilder
import io.kvision.core.Container
import io.kvision.core.Widget
import io.kvision.core.getElementJQuery
import io.kvision.core.getElementJQueryD
import io.kvision.form.Form
import io.kvision.form.FormInput
import io.kvision.form.FormPanel
import io.kvision.form.GenericFormComponent
import io.kvision.form.InputSize
import io.kvision.form.ValidationStatus
import io.kvision.i18n.I18n
import io.kvision.state.MutableState
import io.kvision.types.KFile
import io.kvision.utils.getContent
import io.kvision.utils.obj
import org.w3c.files.File
import kotlin.collections.set
import kotlin.reflect.KProperty1

/**
 * The file upload component.
 *
 * @constructor
 * @param uploadUrl the optional URL for the upload processing action
 * @param multiple determines if multiple file upload is supported
 * @param className CSS class names
 * @param init an initializer extension function
 */
@Suppress("TooManyFunctions")
open class UploadInput(
    uploadUrl: String? = null,
    multiple: Boolean = false,
    className: String? = null,
    init: (UploadInput.() -> Unit)? = null
) :
    Widget((className?.let { "$it " } ?: "") + "form-control"), GenericFormComponent<List<KFile>?>, FormInput,
    MutableState<List<KFile>?> {

    protected val observers = mutableListOf<(List<KFile>?) -> Unit>()

    /**
     * Temporary external value (used in tests)
     */
    protected var tmpValue: List<KFile>? = null

    /**
     * File input value.
     */
    override var value: List<KFile>?
        get() = getValue()
        set(value) {
            if (value == null) resetInput()
            tmpValue = value
            observers.forEach { ob -> ob(value) }
        }

    /**
     * The optional URL for the upload processing action.
     * If not set the upload button action will default to form submission.
     */
    var uploadUrl: String? by refreshOnUpdate(uploadUrl) { refreshUploadInput() }

    /**
     * Determines if multiple file upload is supported.
     */
    var multiple: Boolean by refreshOnUpdate(multiple) { refresh(); refreshUploadInput() }

    /**
     * The extra data that will be passed as data to the AJAX server call via POST.
     */
    var uploadExtraData: ((String, Int) -> dynamic)? by refreshOnUpdate { refreshUploadInput() }

    /**
     * Determines if the explorer theme is used.
     */
    var explorerTheme: Boolean by refreshOnUpdate(false) { refreshUploadInput() }

    /**
     * Determines if the input selection is required.
     */
    var required: Boolean by refreshOnUpdate(false) { refreshUploadInput() }

    /**
     * Determines if the caption is shown.
     */
    var showCaption: Boolean by refreshOnUpdate(true) { refreshUploadInput() }

    /**
     * Determines if the preview is shown.
     */
    var showPreview: Boolean by refreshOnUpdate(true) { refreshUploadInput() }

    /**
     * Determines if the remove button is shown.
     */
    var showRemove: Boolean by refreshOnUpdate(true) { refreshUploadInput() }

    /**
     * Determines if the upload button is shown.
     */
    var showUpload: Boolean by refreshOnUpdate(true) { refreshUploadInput() }

    /**
     * Determines if the cancel button is shown.
     */
    var showCancel: Boolean by refreshOnUpdate(true) { refreshUploadInput() }

    /**
     * Determines if the file browse button is shown.
     */
    var showBrowse: Boolean by refreshOnUpdate(true) { refreshUploadInput() }

    /**
     * Determines if the click on the preview zone opens file browse window.
     */
    var browseOnZoneClick: Boolean by refreshOnUpdate(true) { refreshUploadInput() }

    /**
     * Determines if the iconic preview is prefered.
     */
    var preferIconicPreview: Boolean by refreshOnUpdate(false) { refreshUploadInput() }

    /**
     * Allowed file types.
     */
    var allowedFileTypes: Set<String>? by refreshOnUpdate { refreshUploadInput() }

    /**
     * Allowed file extensions.
     */
    var allowedFileExtensions: Set<String>? by refreshOnUpdate { refreshUploadInput() }

    /**
     * Determines if Drag&Drop zone is enabled.
     */
    var dropZoneEnabled: Boolean by refreshOnUpdate(true) { refreshUploadInput() }

    /**
     * The placeholder for the upload control.
     */
    var placeholder: String? by refreshOnUpdate { refreshUploadInput() }

    /**
     * The name attribute of the generated HTML input element.
     */
    override var name: String? by refreshOnUpdate()

    /**
     * Determines if the field is disabled.
     */
    override var disabled by refreshOnUpdate(false) { refresh(); refreshUploadInput() }

    /**
     * The size of the input (currently not working)
     */
    override var size: InputSize? by refreshOnUpdate()

    /**
     * The validation status of the input.
     */
    override var validationStatus: ValidationStatus? by refreshOnUpdate()

    private val nativeFiles: MutableMap<KFile, File> = mutableMapOf()

    init {
        KVManagerUpload.init()
        @Suppress("LeakingThis")
        init?.invoke(this)
    }

    override fun render(): VNode {
        return render("input")
    }

    override fun buildClassSet(classSetBuilder: ClassSetBuilder) {
        super.buildClassSet(classSetBuilder)
        classSetBuilder.add(validationStatus)
        classSetBuilder.add(size)
    }

    override fun buildAttributeSet(attributeSetBuilder: AttributeSetBuilder) {
        super.buildAttributeSet(attributeSetBuilder)
        attributeSetBuilder.add("type", "file")
        name?.let {
            attributeSetBuilder.add("name", it)
        }
        if (multiple) {
            attributeSetBuilder.add("multiple", "true")
        }
        if (disabled) {
            attributeSetBuilder.add("disabled")
        }
    }

    private fun getValue(): List<KFile>? {
        val v = getFiles() ?: tmpValue
        return if (v.isNullOrEmpty()) null else v
    }

    @Suppress("UnsafeCastFromDynamic")
    override fun afterInsert(node: VNode) {
        getElementJQueryD()?.fileinput(getSettingsObj())
        getElementJQuery()?.parent()?.parent()?.parent()?.find("div.kv-fileinput-caption")?.removeAttr("tabindex")
        getElementJQuery()?.parent()?.parent()?.parent()?.find("input.file-caption-name")?.attr("tabindex", "-1")
        getElementJQuery()?.parent()?.parent()?.parent()?.find("button.fileinput-remove")?.removeAttr("tabindex")
        getElementJQuery()?.parent()?.parent()?.parent()?.find("div.btn-file")?.removeAttr("tabindex")
        if (uploadUrl != null) {
            this.getElementJQuery()?.on("fileselect") { e, _ ->
                this.dispatchEvent("fileSelectUpload", obj { detail = e })
            }
            this.getElementJQuery()?.on("fileclear") { e, _ ->
                this.dispatchEvent("fileClearUpload", obj { detail = e })
            }
            this.getElementJQuery()?.on("filereset") { e, _ ->
                this.dispatchEvent("fileResetUpload", obj { detail = e })
            }
            this.getElementJQuery()?.on("filebrowse") { e, _ ->
                this.dispatchEvent("fileBrowseUpload", obj { detail = e })
            }
            this.getElementJQueryD()?.on("filepreupload") lambda@{ _, data, previewId, index ->
                data["previewId"] = previewId
                data["index"] = index
                this.dispatchEvent("filePreUpload", obj { detail = data })
                return@lambda null
            }
        }
        this.getElementJQuery()?.on("focus") { _, _ ->
            getElementJQuery()?.parent()?.parent()?.parent()?.addClass("kv-focus")
        }
        this.getElementJQuery()?.on("blur") { _, _ ->
            getElementJQuery()?.parent()?.parent()?.parent()?.removeClass("kv-focus")
        }
    }

    override fun afterDestroy() {
        getElementJQueryD()?.fileinput("destroy")
    }

    private fun refreshUploadInput() {
        getElementJQueryD()?.fileinput("refresh", getSettingsObj())
    }

    /**
     * Resets the file input control.
     */
    open fun resetInput() {
        getElementJQueryD()?.fileinput("reset")
    }

    /**
     * Clears the file input control (including the native input).
     */
    open fun clearInput() {
        getElementJQueryD()?.fileinput("clear")
    }

    /**
     * Trigger ajax upload (only for ajax mode).
     */
    open fun upload() {
        getElementJQueryD()?.fileinput("upload")
    }

    /**
     * Cancel an ongoing ajax upload (only for ajax mode).
     */
    open fun cancel() {
        getElementJQueryD()?.fileinput("cancel")
    }

    /**
     * Locks the file input (disabling all buttons except a cancel button).
     */
    open fun lock() {
        getElementJQueryD()?.fileinput("lock")
    }

    /**
     * Unlocks the file input.
     */
    open fun unlock() {
        getElementJQueryD()?.fileinput("unlock")
    }

    /**
     * Returns the native JavaScript File object.
     * @param kFile KFile object
     * @return File object
     */
    fun getNativeFile(kFile: KFile): File? {
        return nativeFiles[kFile]
    }

    private fun getFiles(): List<KFile>? {
        nativeFiles.clear()
        val fileStack = getElementJQueryD()?.fileinput("getFileStack")
        return if (fileStack != null) {
            val list = mutableListOf<KFile>()
            for (key in js("Object").keys(fileStack)) {
                val nativeFile = fileStack[key].file as File
                val kFile = KFile(nativeFile.name, nativeFile.size.toInt(), null)
                nativeFiles[kFile] = nativeFile
                list += kFile
            }
            list
        } else {
            null
        }
    }

    /**
     * Returns the value of the file input control as a String.
     * @return value as a String
     */
    fun getValueAsString(): String? {
        return value?.joinToString(",") { it.name }
    }

    private fun getSettingsObj(): dynamic {
        val language = I18n.language
        return obj {
            this.uploadUrl = uploadUrl
            this.uploadExtraData = uploadExtraData ?: undefined
            this.theme = if (explorerTheme) "explorer-fas" else "bs5"
            this.required = required
            this.showCaption = showCaption
            this.showPreview = showPreview
            this.showRemove = showRemove
            this.showUpload = showUpload
            this.showCancel = showCancel
            this.showBrowse = showBrowse
            this.browseOnZoneClick = browseOnZoneClick
            this.preferIconicPreview = preferIconicPreview
            this.allowedFileTypes = allowedFileTypes?.toTypedArray()
            this.allowedFileExtensions = allowedFileExtensions?.toTypedArray()
            this.dropZoneEnabled = dropZoneEnabled
            this.fileActionSettings = obj {
                this.showUpload = showUpload
                this.showRemove = showRemove
            }
            this.autoOrientImage = false
            this.purifyHtml = false
            if (placeholder != null) this.msgPlaceholder = placeholder
            this.language = language
        }
    }

    override fun getState(): List<KFile>? = value

    override fun subscribe(observer: (List<KFile>?) -> Unit): () -> Unit {
        observers += observer
        observer(value)
        return {
            observers -= observer
        }
    }

    override fun setState(state: List<KFile>?) {
        value = state
    }
}

/**
 * DSL builder extension function.
 *
 * It takes the same parameters as the constructor of the built component.
 */
fun Container.uploadInput(
    uploadUrl: String? = null,
    multiple: Boolean = false,
    className: String? = null,
    init: (UploadInput.() -> Unit)? = null
): UploadInput {
    val uploadInput = UploadInput(uploadUrl, multiple, className, init)
    this.add(uploadInput)
    return uploadInput
}

/**
 * Returns file with the content read.
 * @param key key identifier of the control
 * @param kFile object identifying the file
 * @return KFile object
 */
suspend fun <K : Any> Form<K>.getContent(
    key: KProperty1<K, List<KFile>?>,
    kFile: KFile
): KFile {
    val control = getControl(key) as Upload
    val content = control.getNativeFile(kFile)?.getContent()
    return kFile.copy(content = content)
}


/**
 * Returns file with the content read.
 * @param key key identifier of the control
 * @param kFile object identifying the file
 * @return KFile object
 */
suspend fun <K : Any> FormPanel<K>.getContent(
    key: KProperty1<K, List<KFile>?>,
    kFile: KFile
): KFile {
    val control = getControl(key) as Upload
    val content = control.getNativeFile(kFile)?.getContent()
    return kFile.copy(content = content)
}
