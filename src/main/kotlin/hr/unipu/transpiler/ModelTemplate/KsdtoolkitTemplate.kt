package hr.unipu.transpiler.ModelTemplate

var descriptionPackage="/**\n" +
        " * SD model of Simple Compound Interest.\n" +
        " *\n" +
        " * @author [Siniša Sovilj](mailto:sinisa.sovilj@unipu.hr)\n" +
        " */\n" +
        "\n" +
        "package hr.unipu.transpiler.Kotlin_models"

var constantValues="\n" +
        "open class ModelGenericSD : Model() {\n" +
        "\n" +
        "    // Static properties:\n" +
        "    companion object {\n" +
        "        const val CONSTANT_KEY = \"CONSTANT\"\n" +
        "        const val CONVERTER_KEY = \"converter\"\n" +
        "        const val INITIAL_STOCK_KEY = \"INITIAL_STOCK\"\n" +
        "        const val INFLOW_KEY = \"inflow\"\n" +
        "        const val OUTFLOW_KEY = \"outflow\"\n" +
        "        const val STOCK_KEY = \"Stock\"\n" +
        "\n" +
        "        const val CONSTANT_VALUE = 10          // [%]\n" +
        "        const val INITIAL_STOCK_VALUE = 100    // [€]\n" +
        "        const val INITIAL_TIME_VALUE = 0       // [month]\n" +
        "        const val FINAL_TIME_VALUE = 120       // [month]\n" +
        "        const val TIME_STEP_VALUE = 0.25       // [month]\n" +
        "    }"
var initializationOfModel="init {\n" +
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
        "        model.timeStep = TIME_STEP_VALUE\n" +
        "        model.integrationType = EulerIntegration()\n" +
        "        model.name = \"Generic SD Model\"   // name is optional\n" +
        "        model.timeUnit = \"month\"          // unit is optional\n" +
        "\n" +
        "\n" +
        "\n" +
        "        // 2. Create all system elements:\n" +
        "\n" +
        "        // - 2a. Variables (Constants)\n" +
        "        val CONSTANT = model.constant(CONSTANT_KEY)\n" +
        "        val INITIAL_STOCK = model.constant(INITIAL_STOCK_KEY)\n" +
        "\n" +
        "        // - 2b. Variables (Converters)\n" +
        "        val converter = model.converter(CONVERTER_KEY)\n" +
        "\n" +
        "        // - 2c. Stocks\n" +
        "        val Stock = model.stock(STOCK_KEY)\n" +
        "\n" +
        "        // - 2d. Flows\n" +
        "        val inflow = model.flow(INFLOW_KEY)\n" +
        "        val outflow = model.flow(OUTFLOW_KEY)\n" +
        "\n" +
        "        // - 2e. Modules\n" +
        "        val Module = model.createModule(\n" +
        "            \"Module\",\n" +
        "            \"hr.unipu.ksdtoolkit.modules.ModuleGenericCompoundDecrease\"\n" +
        "        ) as ModuleGenericCompoundDecrease\n" +
        "\n" +
        "\n" +
        "        // - 2f. (Optional): Entities' descriptions\n" +
        "        CONSTANT.description = \"Annual flow rate in [%/year]\"\n" +
        "        INITIAL_STOCK.description = \"Initial capital in [EUR] in the beginning of the simulation.\"\n" +
        "        converter.description = \"Converts percentage to decimal.\"\n" +
        "        Stock.description = \"Accumulated capital in [EUR] at specific point in time.\"\n" +
        "        inflow.description = \"Interest inflow in [EUR / chosen unit of time], e.g. [EUR/month].\"\n" +
        "        outflow.description = \"Interest outflow in [EUR / chosen unit of time], e.g. [EUR/month].\"\n" +
        "\n" +
        "        // - 2g. (Optional): Entities' units\n" +
        "        CONSTANT.unit = \"%/year\"\n" +
        "        INITIAL_STOCK.unit = \"€\"\n" +
        "        Stock.unit = \"€\"\n" +
        "        inflow.unit = \"€/month\"\n" +
        "        outflow.unit = \"€/month\"\n" +
        "\n" +
        "\n" +
        "        // 3. Initial values:\n" +
        "\n" +
        "        // - 3a. Stocks\n" +
        "        Stock.initialValue = { INITIAL_STOCK }      // Accepts: Double, Int or ModelEntity\n" +
        "\n" +
        "\n" +
        "\n" +
        "        // 4. Equations:\n" +
        "\n" +
        "        // - 4a. Constants\n" +
        "        CONSTANT.equation = { CONSTANT_VALUE }\n" +
        "        INITIAL_STOCK.equation = { INITIAL_STOCK_VALUE }\n" +
        "\n" +
        "        // - 4b. Converters\n" +
        "        converter.equation = { CONSTANT / 100.0 / 12.0 }\n" +
        "\n" +
        "        // - 4c. Stocks\n" +
        "        Stock.equation = { inflow - outflow }     // Function type can be either Double or ModelEntity.\n" +
        "\n" +
        "        // - 4d. Flows:\n" +
        "        inflow.equation = { Stock * converter }    // Simplified converters so that only equations are used.\n" +
        "        outflow.equation = { Module.outflow }      // Alternative, instead of lambda, member reference: Module::outflow\n" +
        "\n" +
        "        // - 4e. Modules:\n" +
        "        Module.inflow.equation = { outflow }\n" +
        "\n" +
        "\n" +
        "    }\n" +
        "\n" +
        "}"

var KSDtemplate = descriptionPackage + constantValues + initializationOfModel

fun getKSDTemplate():String{
        return KSDtemplate
}