package com.tzh.mamp.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.tzh.mamp.data.model.Consonant
import com.tzh.mamp.data.model.VowelLetter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.internal.immutableListOf
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MyanmarLetterRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {


    // Independent vowels with dependent equivalents
    val vowelLetters = listOf(
        VowelLetter(
            id = 1,
            symbol = "အ - အာ - အား",
            sound = "/ʔa/",
            example = "အမ (mother)",
            equivalent = "အ",
            fileLink = "vowels/ah.mp3"
        ),
        VowelLetter(
            id = 2,
            symbol = "အိ - အီ - အီး",
            sound = "/ʔi/",
            example = "ဤ (this)",
            equivalent = "အိ",
            fileLink = "vowels/e.mp3"
        ),
        VowelLetter(
            id = 3,
            symbol = "ဥ - အူ - ဦး",
            sound = "/ʔu/",
            example = "ဥ (egg)",
            equivalent = "ဥ",
            fileLink = "vowels/u.mp3"
        ),
        VowelLetter(
            id = 4,
            symbol = "အေ့ - အေ - အေး",
            sound = "/ʔe/",
            example = "အေး (cool)",
            equivalent = "အေ",
            fileLink = "vowels/ay.mp3"
        ),
        VowelLetter(
            id = 5,
            symbol = "ဧည့် - အယ် - အဲ",
            sound = "/ʔɛ/",
            example = "ဧရာဝတီ (Ayeyarwady River)",
            equivalent = "အဲ",
            fileLink = "vowels/eare.mp3"
        ),
        VowelLetter(
            id = 6,
            symbol = "အော့ - အော် - ဩ",
            sound = "/ʔɔ/",
            example = "ဩဇာ (influence)",
            equivalent = "အော",
            fileLink = "vowels/au.mp3"
        ),
        VowelLetter(
            id = 7,
            symbol = "အံ့ - အန် - အမ်း",
            sound = "/ʔan/",
            example = "အံ့ (surprise)",
            equivalent = "အန်",
            fileLink = "vowels/an.mp3"
        ),
        VowelLetter(
            id = 8,
            symbol = "အို့ - အို - အိုး",
            sound = "/ʔo/",
            example = "အိုး (pot)",
            equivalent = "အို",
            fileLink = "vowels/o.mp3"
        ),
        VowelLetter(
            id = 9,
            symbol = "အင့် - အင် - အင်း",
            sound = "/ʔin/",
            example = "အင်း (yes)",
            equivalent = "အင်",
            fileLink = "vowels/in.mp3"
        ),
        VowelLetter(
            id = 10,
            symbol = "အောင့် - အောင် - အောင်း",
            sound = "/ʔaʊɴ/",
            example = "အောင် (success)",
            equivalent = "အောင်",
            fileLink = "vowels/oun.mp3"
        ),
        VowelLetter(
            id = 11,
            symbol = "အိုင့် - အိုင် - အိုင်း",
            sound = "/ʔaiɴ/",
            example = "အိုင် (pond)",
            equivalent = "အိုင်",
            fileLink = "vowels/ine.mp3"
        ),
        VowelLetter(
            id = 12,
            symbol = "အိမ့် - အိမ် - အိမ်း",
            sound = "/ʔeiɴ/",
            example = "အိမ် (house)",
            equivalent = "အိမ်",
            fileLink = "vowels/ain.mp3"
        ),
        VowelLetter(
            id = 13,
            symbol = "အုန့် - အုန် - အုန်း",
            sound = "/ʔouɴ/",
            example = "အုန်း (coconut)",
            equivalent = "အုန်း",
            fileLink = "vowels/ome.mp3"
        ),
        VowelLetter(
            id = 14,
            symbol = "အွန့် - အွန် - အွန်း",
            sound = "/ʔuɴ/",
            example = "အွန်း (warm)",
            equivalent = "အွန်း",
            fileLink = "vowels/oon.mp3"
        )
    )


    val consonants = immutableListOf(
        Consonant(id = 1, "က", "/ka/", "ကလေး (child)", "Picture of a smiling child"),
        Consonant(id = 2, "ခ", "/kʰa/", "ခရမ်းချဉ် (eggplant)", "Purple eggplant"),
        Consonant(id = 3, "ဂ", "/ga/", "ဂီတ (music)", "Musical notes or guitar"),
        Consonant(id = 4, "ဃ", "/gʰa/", "ဃနာ (rare word)", "Optional or skip for simplicity"),
        Consonant(id = 5, "င", "/ŋa/", "ငါး (fish)", "Cartoon fish swimming"),
        Consonant(id = 6, "စ", "/sa/", "စက် (machine)", "Toy robot or gears"),
        Consonant(id = 7, "ဆ", "/sʰa/", "ဆရာ (teacher)", "Teacher with chalkboard"),
        Consonant(id = 8, "ဇ", "/za/", "ဇာတ်ကား (movie)", "Film reel or cinema screen"),
        Consonant(id = 9, "ဈ", "/zʰa/", "ဈေး (market)", "Market stall with fruits"),
        Consonant(id = 10, "ည", "/ɲa/", "ညစာ (dinner)", "Plate of food"),
        Consonant(id = 11, "ဋ", "/ṭa/", "ဋီ (rare)", "Optional or skip"),
        Consonant(id = 12, "ဌ", "/ṭʰa/", "ဌာန (department)", "Office building icon"),
        Consonant(id = 13, "ဍ", "/ḍa/", "ဍာ (rare)", "Optional or skip"),
        Consonant(id = 14, "ဎ", "/ḍʰa/", "ဎန (rare)", "Optional or skip"),
        Consonant(id = 15, "ဏ", "/ṇa/", "ဏာ (rare)", "Optional or skip"),
        Consonant(id = 16, "တ", "/ta/", "တံတား (bridge)", "Cartoon bridge over river"),
        Consonant(id = 17, "ထ", "/tʰa/", "ထမင်း (rice)", "Bowl of rice"),
        Consonant(id = 18, "ဒ", "/da/", "ဒေါက်တာ (doctor)", "Doctor with stethoscope"),
        Consonant(id = 19, "ဓ", "/dʰa/", "ဓာတ်ပုံ (photo)", "Camera or photo frame"),
        Consonant(id = 20, "န", "/na/", "နေ (sun)", "Bright sun icon"),
        Consonant(id = 21, "ပ", "/pa/", "ပန်း (flower)", "Colorful flower"),
        Consonant(id = 22, "ဖ", "/pʰa/", "ဖရဲသီး (watermelon)", "Watermelon slice"),
        Consonant(id = 23, "ဗ", "/ba/", "ဗီဒီယို (video)", "Play button or screen"),
        Consonant(id = 24, "ဘ", "/bʰa/", "ဘုရား (temple)", "Pagoda or temple icon"),
        Consonant(id = 25, "မ", "/ma/", "မိခင် (mother)", "Mother and child"),
        Consonant(id = 26, "ယ", "/ya/", "ယာဉ် (vehicle)", "Car or bus"),
        Consonant(id = 27, "ရ", "/ya/", "ရေ (water)", "Splash or water droplet"),
        Consonant(id = 28, "လ", "/la/", "လ (moon)", "Crescent moon"),
        Consonant(id = 29, "ဝ", "/wa/", "ဝက် (pig)", "Cartoon pig"),
        Consonant(id = 30, "သ", "/θa/", "သစ်ပင် (tree)", "Tree with leaves"),
        Consonant(id = 31, "ဟ", "/ha/", "ဟင်း (curry)", "Bowl of curry"),
        Consonant(id = 32, "ဠ", "/ḷa/", "ဠာ (rare)", "Optional or skip"),
        Consonant(id = 33, "အ", "/ʔa/", "အိမ် (house)", "House icon")
    )
    private val alphabetsCollection = firestore.collection("Alphabets")
    private val vowelsCollection = firestore.collection("Vowels")


    init {
        CoroutineScope(Dispatchers.IO).launch {
//            getData()
//            setList()
        }
    }

    suspend fun deleteAllAlphabet() {
        firestore.deleteAllDocumentsInCollection(alphabetsCollection)
    }

    suspend fun setList() {
        uploadCollection(vowelsCollection, vowelLetters)
    }

    suspend fun getData(): List<Consonant> {
        val productSnapshot = alphabetsCollection.orderBy("id").get().await()
        val products = productSnapshot.documents.mapNotNull { it.toObject<Consonant>() }
//        consonants.addAll(products)
        return products
    }

    private suspend inline fun <reified T : Any> uploadCollection(
        collection: CollectionReference, dataList: List<T>
    ) {
        dataList.forEach { data ->
            collection.add(data).await()
        }
    }

    private suspend fun FirebaseFirestore.deleteAllDocumentsInCollection(collection: CollectionReference): Result<Unit> {
        return try {
            val snapshot = collection.get().await()
            this.runBatch { batch ->
                snapshot.documents.forEach { document ->
                    batch.delete(document.reference)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}