package com.joelzhu.lib.scanner.plugin.code.cache.bean

import com.joelzhu.lib.scanner.annotation.internal.CompileScanBean

data class CodeGenerateBean(
    val normalData: HashMap<Int, ArrayList<CompileScanBean>>,

    val defaultData: HashMap<Int, ArrayList<CompileScanBean>>
)