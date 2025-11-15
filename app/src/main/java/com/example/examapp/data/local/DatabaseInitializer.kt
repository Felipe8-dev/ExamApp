package com.example.examapp.data.local

import android.content.Context
import com.example.examapp.data.local.entities.LocalQuestion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseInitializer @Inject constructor(
    private val database: ExamDatabase
) {
    
    fun initializeIfNeeded() {
        CoroutineScope(Dispatchers.IO).launch {
            val questionCount = database.questionDao().getQuestionCount()
            if (questionCount == 0) {
                loadInitialQuestions()
            }
        }
    }
    
    private suspend fun loadInitialQuestions() {
        val questions = createInitialQuestions()
        database.questionDao().insertQuestions(questions)
    }
    
    private fun createInitialQuestions(): List<LocalQuestion> {
        return listOf(
            // 5 preguntas sobre conceptos básicos de sistemas operativos
            LocalQuestion(
                text = "¿Qué es un sistema operativo?",
                category = "basicos",
                correctAnswerIndex = 0,
                option1 = "Software que gestiona los recursos del hardware y proporciona servicios a los programas",
                option2 = "Un tipo de procesador",
                option3 = "Un dispositivo de almacenamiento",
                option4 = "Un lenguaje de programación"
            ),
            LocalQuestion(
                text = "¿Cuál es la función principal del kernel en un sistema operativo?",
                category = "basicos",
                correctAnswerIndex = 1,
                option1 = "Gestionar la interfaz gráfica",
                option2 = "Gestionar los recursos del sistema y la comunicación entre hardware y software",
                option3 = "Ejecutar aplicaciones de usuario",
                option4 = "Almacenar archivos"
            ),
            LocalQuestion(
                text = "¿Qué significa la sigla OS?",
                category = "basicos",
                correctAnswerIndex = 0,
                option1 = "Operating System",
                option2 = "Output System",
                option3 = "Optical Storage",
                option4 = "Operating Software"
            ),
            LocalQuestion(
                text = "¿Cuál es el sistema operativo más utilizado en servidores?",
                category = "basicos",
                correctAnswerIndex = 2,
                option1 = "Windows",
                option2 = "macOS",
                option3 = "Linux",
                option4 = "Unix"
            ),
            LocalQuestion(
                text = "¿Qué es la multiprogramación?",
                category = "basicos",
                correctAnswerIndex = 0,
                option1 = "Capacidad de ejecutar múltiples programas simultáneamente",
                option2 = "Usar varios procesadores",
                option3 = "Tener múltiples usuarios",
                option4 = "Almacenar varios archivos"
            ),
            
            // 5 preguntas sobre estructura y características de sistemas operativos modernos
            LocalQuestion(
                text = "¿Qué es un sistema de archivos?",
                category = "modernos",
                correctAnswerIndex = 1,
                option1 = "Un tipo de memoria RAM",
                option2 = "Método para organizar y almacenar archivos en un dispositivo",
                option3 = "Un procesador",
                option4 = "Un tipo de red"
            ),
            LocalQuestion(
                text = "¿Qué es la memoria virtual?",
                category = "modernos",
                correctAnswerIndex = 2,
                option1 = "Memoria RAM física",
                option2 = "Disco duro",
                option3 = "Técnica que permite usar espacio en disco como extensión de la RAM",
                option4 = "Memoria caché"
            ),
            LocalQuestion(
                text = "¿Qué es un proceso en un sistema operativo?",
                category = "modernos",
                correctAnswerIndex = 0,
                option1 = "Un programa en ejecución con su propio espacio de memoria",
                option2 = "Un archivo",
                option3 = "Un usuario",
                option4 = "Un dispositivo"
            ),
            LocalQuestion(
                text = "¿Qué es un hilo (thread)?",
                category = "modernos",
                correctAnswerIndex = 3,
                option1 = "Un proceso independiente",
                option2 = "Un archivo",
                option3 = "Un dispositivo",
                option4 = "Unidad de ejecución más pequeña dentro de un proceso"
            ),
            LocalQuestion(
                text = "¿Qué es la planificación de procesos?",
                category = "modernos",
                correctAnswerIndex = 1,
                option1 = "Crear nuevos procesos",
                option2 = "Decidir qué proceso ejecutar y cuándo",
                option3 = "Eliminar procesos",
                option4 = "Almacenar procesos"
            ),
            
            // 5 preguntas sobre sistemas operativos en dispositivos móviles
            LocalQuestion(
                text = "¿Cuál es el sistema operativo móvil más utilizado en el mundo?",
                category = "moviles",
                correctAnswerIndex = 0,
                option1 = "Android",
                option2 = "iOS",
                option3 = "Windows Mobile",
                option4 = "BlackBerry OS"
            ),
            LocalQuestion(
                text = "¿Qué es iOS?",
                category = "moviles",
                correctAnswerIndex = 2,
                option1 = "Un sistema operativo de Google",
                option2 = "Un tipo de procesador",
                option3 = "Sistema operativo móvil desarrollado por Apple",
                option4 = "Un lenguaje de programación"
            ),
            LocalQuestion(
                text = "¿Qué característica es común en los sistemas operativos móviles modernos?",
                category = "moviles",
                correctAnswerIndex = 1,
                option1 = "Solo funcionan sin internet",
                option2 = "Soporte para aplicaciones, multitarea y conectividad",
                option3 = "No tienen interfaz gráfica",
                option4 = "Solo funcionan con un tipo de hardware"
            ),
            LocalQuestion(
                text = "¿Qué es Android?",
                category = "moviles",
                correctAnswerIndex = 3,
                option1 = "Un dispositivo móvil",
                option2 = "Un procesador",
                option3 = "Una aplicación",
                option4 = "Sistema operativo móvil basado en Linux desarrollado por Google"
            ),
            LocalQuestion(
                text = "¿Qué es una app en sistemas operativos móviles?",
                category = "moviles",
                correctAnswerIndex = 0,
                option1 = "Aplicación móvil diseñada para ejecutarse en dispositivos móviles",
                option2 = "Un tipo de archivo",
                option3 = "Un sistema operativo",
                option4 = "Un procesador"
            ),
            
            // 5 preguntas sobre seguridad en sistemas operativos
            LocalQuestion(
                text = "¿Qué es un firewall?",
                category = "seguridad",
                correctAnswerIndex = 1,
                option1 = "Un tipo de antivirus",
                option2 = "Sistema que controla el tráfico de red y bloquea accesos no autorizados",
                option3 = "Un tipo de memoria",
                option4 = "Un procesador"
            ),
            LocalQuestion(
                text = "¿Qué es la autenticación en un sistema operativo?",
                category = "seguridad",
                correctAnswerIndex = 2,
                option1 = "Almacenar datos",
                option2 = "Ejecutar programas",
                option3 = "Proceso de verificar la identidad de un usuario",
                option4 = "Conectar a internet"
            ),
            LocalQuestion(
                text = "¿Qué es un virus informático?",
                category = "seguridad",
                correctAnswerIndex = 0,
                option1 = "Programa malicioso que se replica y puede dañar el sistema",
                option2 = "Un tipo de hardware",
                option3 = "Un sistema operativo",
                option4 = "Un tipo de archivo"
            ),
            LocalQuestion(
                text = "¿Qué es la encriptación?",
                category = "seguridad",
                correctAnswerIndex = 3,
                option1 = "Eliminar archivos",
                option2 = "Copiar archivos",
                option3 = "Comprimir archivos",
                option4 = "Proceso de convertir datos en un formato ilegible para protegerlos"
            ),
            LocalQuestion(
                text = "¿Qué es un parche de seguridad?",
                category = "seguridad",
                correctAnswerIndex = 1,
                option1 = "Un nuevo sistema operativo",
                option2 = "Actualización que corrige vulnerabilidades de seguridad",
                option3 = "Un tipo de antivirus",
                option4 = "Un dispositivo de hardware"
            )
        )
    }
}

