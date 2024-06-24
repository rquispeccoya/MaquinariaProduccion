package com.example.maquinariaproduccion;

public class Data {

    static final String[] factorExterno = {"Manipulador de materiales",
            "Mezclados", "Hasta 3 mm (1/8\")",
            "De 3 mm (1/8\") a 20 mm (3/4\")",
            "De 20 mm (3/4\") a 150 mm (6\")",
            "Más de 150 mm (6\")",
            "Banco o fracturado",
            "Apilado por transportador o topadora a más de 3 m (10 pies)",
            "Apilado por transportador o topadora a menos de 3 m (10 pies)",
            "Descargado por camión", "El mismo propietario de camiones y cargadores",
            "Propietario independiente de camiones",
            "Operación constante", "Operación intermitente",
            "Punto de carga pequeño", "Punto de carga frágil"};

    static final double[] factorExternoValor={-0.05,0.02,0.02,-0.02,0,0.03,0.04,0,0.01,0.02,-0.04,0.04,-0.04,0.04,0.04f,0.05};

    static final String[] tipomaquina = {"994F"};
    static final String[][] tamanoCucharon = {{"Cucharon de 5.650mm", "Cucharon de 6.220mm"}};
    static final String[][][] tipoCucharon = {
            {
                    {"Cucharon en V para rocas con dientes y segmentos"},
                    {"Cucharon en V para rocas con dientes y segmentos", "Cucharon para carbon con borde recto"}

            }
    };
    static final double[][][][] capacidadNominal = {
            {
                    {{19,15,5.640,5.556,2.266,5.206,108,16.894,10.911,25.484,128.499,111.091,989,193.779}},
                    {{19,15,6.2,5.563,2.278,5.21,108,16.898,10.636,26.004,126.522,109.241,995,194.729},
                            {31,27,6.2,5.635,2.306,5.179,63,16.830,11.845,26.146,129.295,111.719,974,195.169}}
            }
    };
    static final String[] rendimiento ={"Capacidad nominal del cucharón (§)",
            "Capacidad a ras (§)",
            "Ancho del cucharón (§)",
            "Espacio libre de descarga en descargas de levantamiento completo y de 45° (§)",
            "Alcance en descarga de levantamiento completo y de 45° (§)",
            "Alcance con brazos de levantamiento horizontal y cucharón horizontal",
            "Profundidad de excavación (§)",
            "Longitud total (§)",
            "Altura total con el cucharón levantado completamente (§)",
            "Círculo de giro del cargador con el cucharón en posición de acarreo (§)",
            "Carga límite de equilibrio estático máquina derecha** (§)",
            "Carga límite de equilibrio estático. a giro pleno de 40°** (§)",
            "Fuerza de desprendimiento*** (§)",
            "Peso en orden de trabajo** (§)"
    };

    static final  String [] pesoMaterial={"Basalto","Bauxita, Caolin","Caliche","Carnotita, mineral de uranio",
    "Ceniza","Arcilla – en su lecho natura","Arcilla seca","Arcilla mojada","Arcilla y grava – secas",
    "Arcilla y grava – mojadas","Carbón – antracita en bruto","Carbón – antracita lavada","Carbon - ceniza, carbón bituminoso",
    "Carbon - bituminoso en bruto","Carbon - lavado","Roca descompuesta – 75 % roca, 25 % tierra","Roca descompuesta – 50 % roca, 50 % tierra","Roca descompuesta – 25 % roca, 75 % tierra",
    "Tierra – Apisonada y seca","Tierra - excavada y mojada","Tierra - limo",
    "Granito fragmentado","Grava – como sale de cantera","Grava - seca","Grava - seca 6-50 mm (1/4\"-2\")","Grava - mojada 6-50 mm (1/4\"-2\")",
    "Yeso – fragmentado","Yeso - triturado","Hematita, mineral de hierro","Piedra caliza – fragmentada",
    "Piedra caliza – triturada","Magnetita, mineral de hierro","Pirita, mineral de hierro","Arena – seca y suelta",
    "Arena - humeda","Arena mojada","Arena y arcilla – suelta","Arena y arcilla – compactada","Arena y grava – seca","Arena y grava – mojada",
    "Arenisca","Pizarra bituminosa","Escorias fragmentadas","Nieve - seca","Nieve - mojada","Piedra triturada",
    "Taconita","Tierra vegetal","Roca fragmentada","Virutas de madera"};

    static  final  double [][] pesoMaterialValor={
            {1.960,2.970,0.67},{1.420,1.9,0.75},{1.25,2.26,0.55},{1.63,2.2,0.74},{560,860,0.66},{1.66,2.02,0.82},{1.48,1.84,0.81},{1.66,2.08,0.8},{1.42,1.66,0.85},{1.54,1.84,0.85},
            {1.19,1.6,0.74},{1.1,0,0.74},{650,890,0.93},{950,1.28,0.74},{830,0,0.74},{1.96,2.79,0.7},{1.72,2.28,0.75},{1.57,1.96,0.8},{1.51,1.9,0.8},{1.6,2.02,0.79},
            {1.25,1.54,0.81},{1.66,2.73,0.61},{1.93,2.17,0.89},{1.51,1.69,0.89},{1.69,1.9,0.89},{2.02,2.26,0.89},{1.81,3.17,0.57},{1.6,2.79,0.57},{2.45,2.9,0.85},{1.54,2.61,0.59},
            {1.54,0,0},{2.79,3.26,0.85},{2.58,3.03,0.85},{1.42,1.6,0.89},{1.69,1.9,0.89},{1.84,2.08,0.89},{1.6,2.02,0.79},{2.4,0,0},{1.72,1.93,0.89},{2.02,2.23,0.91},
            {1.51,2.52,0.6},{1.25,1.66,0.75},{1.75,2.94,0.6},{130,0,0},{520,0,0},{1.6,2.67,0.6},{1.9,2.7,0.58},{950,1.37,0.7},{1.75,2.61,0.67},{0,0,0}

    };
    static  final  String [] pesoMaterialOpcion={"Suelto","En banco"};

}
