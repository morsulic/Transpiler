package hr.unipu.transpiler.ModelTemplate

var descriptionPackage="/**\n" +
        " * SD model of Simple Compound Interest.\n" +
        " *\n" +
        " * @author [Siniša Sovilj](mailto:sinisa.sovilj@unipu.hr)\n" +
        " */\n" +
        "\n" +
        "package hr.unipu.transpiler.Kotlin_models"

var constantValuesBeginning="\n" +
        "open class ModelGenericSD : Model() {\n" +
        "\n" +
        "    // Static properties:\n" +
        "    companion object {\n"
var initializationOfModelCreateAllSystemElements=
        "\n\ninit {\n" +
        "\n" +
        "        // 1. Create the model (with setup of: time boundaries & time step & integrationType type)\n" +
        "        val model = this   // inheritance: Model()\n" +
        "\n" +
        "        /*\n" +
        "        val model = Model(\n" +
        "            initialTime = 0,\n" +
        "            finalTime = 100,\n" +
        "            timeStep = 0.25,\n" +
        "            integrationType = EulerIntegration()\n" +
        "        )\n" +
        "        */\n" +
        "\n" +
        "\n" +
        "        /*\n" +
        "        val model = Model(0, 100, 0.25, EulerIntegration())\n" +
        "            // alternative: Model(INITIAL_TIME_VALUE, FINAL_TIME_VALUE, TIME_STEP_VALUE, EulerIntegration())\n" +
        "        */\n" +
        "\n" +
        "\n" +
        "        // override default model properties:\n" +
        "        model.initialTime = INITIAL_TIME_VALUE\n" +
        "        model.finalTime = FINAL_TIME_VALUE\n" +
        "        model.timeStep = TIME_STEP_VALUE\n"

val createAllSystemElements =" \n" +
        "\n" +
        "\n" +
        "        // 2. Create all system elements:\n" +
        "\n"

val initialValues="        \n// 3. Initial values:\n" +
        "\n"

val equations=        "        \n// 4. Equations:\n" +
        "\n"

var KSDtemplate = descriptionPackage + constantValuesBeginning + initializationOfModelCreateAllSystemElements

fun getKSDTemplate():String{
        return KSDtemplate
}