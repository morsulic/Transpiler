package hr.unipu.transpiler.globalVariables

const val packageAndImports = "package hr.unipu.ksdtoolkit.models\n" +
        "\n" +
        "import hr.unipu.ksdtoolkit.entities.Model\n" +
        "import hr.unipu.ksdtoolkit.entities.div\n" +
        "import hr.unipu.ksdtoolkit.entities.times\n" +
        "import hr.unipu.ksdtoolkit.integration.EulerIntegration\n" +
        "import hr.unipu.ksdtoolkit.integration.RungeKuttaIntegration\n"


const val constantValuesBeginning = "// Static properties:\n" +
        "companion object {\n"
const val initializationOfModelCreateAllSystemElements =
    "\n\ninit {\n" +
            "\n" +
            "        // 1. Create the model (with setup of: time boundaries & time step & integrationType type)\n" +
            "        val model = this   // inheritance: Model()\n" +
            "\n" +
            "\n" +
            "        // override default model properties:\n" +
            "        model.initialTime = INITIAL_TIME_VALUE\n" +
            "        model.finalTime = FINAL_TIME_VALUE\n" +
            "        model.timeStep = TIME_STEP_VALUE\n"

const val createAllSystemElements = " \n" +
        "\n" +
        "\n" +
        "\t\t// 2. Create all system elements:\n" +
        "\n"

const val initialValues = "\n\t\t// 3. Initial values:\n" +
        "\n"

const val equations = "\n\t\t// 4. Equations:\n" +
        "\n"

