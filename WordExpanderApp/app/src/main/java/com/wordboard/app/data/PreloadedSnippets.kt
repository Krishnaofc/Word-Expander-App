package com.wordboard.app.data

/**
 * Massive pre-populated multilingual library with 100+ categorized shortcuts & templates.
 * Users get all of these immediately upon installing the app, and can also add unlimited custom ones.
 */
object PreloadedSnippets {
    fun getDefaultSnippets(): List<SnippetEntity> {
        val list = mutableListOf<SnippetEntity>()

        // ==========================================
        // 1. DAILY CHAT & SOCIAL (ENGLISH & HINGLISH)
        // ==========================================
        list.addAll(listOf(
            SnippetEntity(
                shortcut = "omw",
                expandedText = "On my way! See you shortly.",
                label = "On My Way",
                category = "Daily Chat",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "gm",
                expandedText = "Good morning! Hope you have a wonderful and productive day ahead ☀️",
                label = "Good Morning",
                category = "Daily Chat",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "gn",
                expandedText = "Good night! Sleep well and sweet dreams 🌙",
                label = "Good Night",
                category = "Daily Chat",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "hru",
                expandedText = "Hey! How have you been doing lately?",
                label = "How Are You",
                category = "Daily Chat",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "tc",
                expandedText = "Take care of yourself! Talk to you soon.",
                label = "Take Care",
                category = "Daily Chat",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "brb",
                expandedText = "Be right back in a few minutes!",
                label = "Be Right Back",
                category = "Daily Chat",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "idk",
                expandedText = "I don't know for sure, let me check and get back to you.",
                label = "I Don't Know",
                category = "Daily Chat",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "imo",
                expandedText = "In my honest opinion,",
                label = "In My Opinion",
                category = "Daily Chat",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "tysm",
                expandedText = "Thank you so much! I really appreciate your help 🙏",
                label = "Thank You So Much",
                category = "Daily Chat",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "yw",
                expandedText = "You are most welcome! Always happy to help.",
                label = "You're Welcome",
                category = "Daily Chat",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "np",
                expandedText = "No problem at all! Glad I could help.",
                label = "No Problem",
                category = "Daily Chat",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "hbd",
                expandedText = "Wishing you a very Happy Birthday! May you have an amazing year filled with success and happiness 🎉🎂",
                label = "Happy Birthday",
                category = "Daily Chat",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "cya",
                expandedText = "See you later! Have a great time.",
                label = "See You",
                category = "Daily Chat",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "asap",
                expandedText = "As soon as possible, please.",
                label = "ASAP",
                category = "Daily Chat",
                languageCode = "en"
            )
        ))

        // ==========================================
        // 2. HINDI & HINGLISH (देवनागरी एवं हिंग्लिश)
        // ==========================================
        list.addAll(listOf(
            SnippetEntity(
                shortcut = "nmt",
                expandedText = "नमस्ते! आप कैसे हैं? आशा है आपका दिन बहुत अच्छा और मंगलमय हो।",
                label = "नमस्ते अभिवादन",
                category = "Hindi",
                languageCode = "hi"
            ),
            SnippetEntity(
                shortcut = "dhny",
                expandedText = "आपका बहुत-बहुत धन्यवाद! आपके बहुमूल्य सहयोग की हम सराहना करते हैं।",
                label = "धन्यवाद",
                category = "Hindi",
                languageCode = "hi"
            ),
            SnippetEntity(
                shortcut = "shubh",
                expandedText = "हार्दिक बधाई एवं ढेरों शुभकामनाएं! ईश्वर आपको सदा सफल और खुश रखे।",
                label = "शुभकामनाएं व बधाई",
                category = "Hindi",
                languageCode = "hi"
            ),
            SnippetEntity(
                shortcut = "ramram",
                expandedText = "राम-राम जी! सब कुशल मंगल? घर-परिवार में सब ठीक?",
                label = "राम-राम अभिवादन",
                category = "Hindi",
                languageCode = "hi"
            ),
            SnippetEntity(
                shortcut = "radhe",
                expandedText = "राधे-राधे! आपका दिन शुभ और सुखमय हो।",
                label = "राधे-राधे",
                category = "Hindi",
                languageCode = "hi"
            ),
            SnippetEntity(
                shortcut = "pranam",
                expandedText = "सादर प्रणाम! आपके आशीर्वाद की कामना करता हूँ।",
                label = "सादर प्रणाम",
                category = "Hindi",
                languageCode = "hi"
            ),
            SnippetEntity(
                shortcut = "shukriya",
                expandedText = "आपका तहे दिल से शुक्रिया! बहुत-बहुत मेहरबानी।",
                label = "शुक्रिया",
                category = "Hindi",
                languageCode = "hi"
            ),
            SnippetEntity(
                shortcut = "alvida",
                expandedText = "अलविदा! अपना बहुत अच्छे से ख्याल रखिएगा, फिर मिलते हैं।",
                label = "अलविदा",
                category = "Hindi",
                languageCode = "hi"
            ),
            SnippetEntity(
                shortcut = "keseho",
                expandedText = "और भाई कैसे हो? सब कैसा चल रहा है आजकल?",
                label = "कैसे हो (Casual)",
                category = "Hindi",
                languageCode = "hi"
            ),
            SnippetEntity(
                shortcut = "thikhai",
                expandedText = "हाँ जी बिल्कुल ठीक है, जैसा आप कहें।",
                label = "ठीक है",
                category = "Hindi",
                languageCode = "hi"
            ),
            SnippetEntity(
                shortcut = "kripya",
                expandedText = "कृपया करके मुझे इसके बारे में थोड़ा और विस्तार से बताएं।",
                label = "कृपया बताएं",
                category = "Hindi",
                languageCode = "hi"
            ),
            SnippetEntity(
                shortcut = "badhai",
                expandedText = "इस शानदार उपलब्धि पर आपको और आपके पूरे परिवार को बहुत-बहुत बधाई!",
                label = "उपलब्धि बधाई",
                category = "Hindi",
                languageCode = "hi"
            )
        ))

        // ==========================================
        // 3. BUSINESS, OFFICE & PROFESSIONAL EMAILS
        // ==========================================
        list.addAll(listOf(
            SnippetEntity(
                shortcut = "/addr",
                expandedText = "Tower B, 7th Floor, Innovation Tech Hub, Sector 62, Noida, UP - 201301, India",
                label = "Complete Office Address",
                category = "Business",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "eml",
                expandedText = "contact.official@mycompany.com",
                label = "Official Work Email",
                category = "Business",
                languageCode = "en",
                triggerMode = "AUTO"
            ),
            SnippetEntity(
                shortcut = "phn",
                expandedText = "+91 98765 43210",
                label = "Primary Mobile Number",
                category = "Business",
                languageCode = "en",
                triggerMode = "AUTO"
            ),
            SnippetEntity(
                shortcut = "/meeting",
                expandedText = "Hi Team,\n\nLet us sync up today at {time} to review our weekly sprint goals and discuss project updates. Looking forward to our discussion.\n\nBest regards,",
                label = "Team Meeting Invite",
                category = "Business",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "/followup",
                expandedText = "Dear Sir/Madam,\n\nI hope this email finds you well. I am following up on our previous discussion regarding the project proposal sent on {date}. Please let me know if you need any additional details.\n\nWarm regards,",
                label = "Email Follow-up Template",
                category = "Business",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "/intro",
                expandedText = "Hello,\n\nMy name is Rahul, and I am reaching out from the Business Development team. We specialize in providing end-to-end software solutions. I would love to schedule a quick 10-minute discovery call with you this week.\n\nBest,",
                label = "Sales Cold Outreach",
                category = "Business",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "/leave",
                expandedText = "Respected Manager,\n\nI am writing to formally request leave for today ({date}) due to urgent personal circumstances. I have delegated my ongoing tasks to my teammate and will remain accessible via email for urgent issues.\n\nSincerely,",
                label = "Casual Leave Request",
                category = "Business",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "/thanks",
                expandedText = "Thank you for reaching out to us. We have received your query and our team will get back to you within 24 business hours.",
                label = "Professional Acknowledgment",
                category = "Business",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "/sig",
                expandedText = "--\nBest regards,\nGopal Sharma | Senior Software Architect\nPhone: +91 98765 43210 | Web: www.example.com",
                label = "Complete Email Signature",
                category = "Business",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "/upi",
                expandedText = "gopal.sharma@okaxis (Please share screenshot after payment)",
                label = "UPI Payment Details",
                category = "Business",
                languageCode = "en"
            )
        ))

        // ==========================================
        // 4. CUSTOMER SUPPORT & E-COMMERCE QUICK REPLIES
        // ==========================================
        list.addAll(listOf(
            SnippetEntity(
                shortcut = "/welcome",
                expandedText = "Welcome to our support! 👋 My name is Alex. How may I assist you today?",
                label = "Customer Welcome",
                category = "Support",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "/refund",
                expandedText = "Your refund request has been initiated successfully. The amount will be credited back to your original payment method within 5-7 working days. Reference ID: #{time}.",
                label = "Refund Confirmation",
                category = "Support",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "/shipping",
                expandedText = "Your order has been dispatched and is on its way! You can track your shipment live using the tracking link sent to your registered mobile number.",
                label = "Shipping Status",
                category = "Support",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "/discount",
                expandedText = "Great news! Use coupon code 'FESTIVE20' at checkout to receive an exclusive 20% instant discount on your entire order valid until this weekend.",
                label = "Discount Voucher",
                category = "Support",
                languageCode = "en"
            ),
            SnippetEntity(
                shortcut = "/hours",
                expandedText = "Our customer support team is available Monday through Saturday from 9:00 AM to 8:00 PM IST. Feel free to drop a message anytime!",
                label = "Business Hours",
                category = "Support",
                languageCode = "en"
            )
        ))

        // ==========================================
        // 5. ARABIC & URDU (العربية و الأردية - RTL)
        // ==========================================
        list.addAll(listOf(
            SnippetEntity(
                shortcut = "slm",
                expandedText = "السلام عليكم ورحمة الله وبركاته، أتمنى لك يوماً سعيداً ومليئاً بالخير.",
                label = "السلام عليكم (تحية)",
                category = "Arabic",
                languageCode = "ar"
            ),
            SnippetEntity(
                shortcut = "shkr",
                expandedText = "شكراً جزيلاً لك على دعمك ومساعدتك القيمة! بارك الله فيك.",
                label = "شكر وتقدير",
                category = "Arabic",
                languageCode = "ar"
            ),
            SnippetEntity(
                shortcut = "afw",
                expandedText = "عفواً، لا شكر على واجب! في خدمتك دائماً.",
                label = "عفواً (أهلاً بك)",
                category = "Arabic",
                languageCode = "ar"
            ),
            SnippetEntity(
                shortcut = "insha",
                expandedText = "إن شاء الله، كل الأمور ستكون على خير ما يرام.",
                label = "إن شاء الله",
                category = "Arabic",
                languageCode = "ar"
            ),
            SnippetEntity(
                shortcut = "masha",
                expandedText = "ما شاء الله تبارك الرحمن! ألف مبروك لك.",
                label = "ما شاء الله",
                category = "Arabic",
                languageCode = "ar"
            ),
            SnippetEntity(
                shortcut = "jazak",
                expandedText = "جزاك الله خيراً وأحسن إليك في الدنيا والآخرة.",
                label = "جزاك الله خيراً",
                category = "Arabic",
                languageCode = "ar"
            )
        ))

        // ==========================================
        // 6. SPANISH (ESPAÑOL)
        // ==========================================
        list.addAll(listOf(
            SnippetEntity(
                shortcut = "hla",
                expandedText = "¡Hola! ¿Cómo estás? Espero que tengas un excelente y productivo día.",
                label = "Saludo en Español",
                category = "Spanish",
                languageCode = "es"
            ),
            SnippetEntity(
                shortcut = "grc",
                expandedText = "¡Muchas gracias por tu amable atención y ayuda! Te lo agradezco de corazón.",
                label = "Agradecimiento",
                category = "Spanish",
                languageCode = "es"
            ),
            SnippetEntity(
                shortcut = "porfa",
                expandedText = "Por favor, ¿podrías compartir más información cuando tengas un momento?",
                label = "Por favor",
                category = "Spanish",
                languageCode = "es"
            ),
            SnippetEntity(
                shortcut = "buenos",
                expandedText = "¡Buenos días! Espero que hayas descansado muy bien.",
                label = "Buenos Días",
                category = "Spanish",
                languageCode = "es"
            ),
            SnippetEntity(
                shortcut = "adios",
                expandedText = "¡Hasta luego! Cuídate mucho y que tengas una gran semana.",
                label = "Despedida",
                category = "Spanish",
                languageCode = "es"
            )
        ))

        // ==========================================
        // 7. JAPANESE (日本語 - KANJI / KANA)
        // ==========================================
        list.addAll(listOf(
            SnippetEntity(
                shortcut = "arig",
                expandedText = "誠にありがとうございます。今後とも何卒よろしくお願い申し上げます。",
                label = "お礼・感謝",
                category = "Japanese",
                languageCode = "ja"
            ),
            SnippetEntity(
                shortcut = "otsk",
                expandedText = "お疲れ様です！本日もご対応いただき、誠にありがとうございました。",
                label = "お疲れ様です",
                category = "Japanese",
                languageCode = "ja"
            ),
            SnippetEntity(
                shortcut = "ohay",
                expandedText = "おはようございます！今日も一日、よろしくお願いいたします。",
                label = "おはようございます",
                category = "Japanese",
                languageCode = "ja"
            ),
            SnippetEntity(
                shortcut = "konn",
                expandedText = "こんにちは！お忙しいところ恐れ入りますが、ご確認のほどよろしくお願いいたします。",
                label = "こんにちは",
                category = "Japanese",
                languageCode = "ja"
            ),
            SnippetEntity(
                shortcut = "yoroshiku",
                expandedText = "どうぞよろしくお願いいたします。",
                label = "よろしくお願い",
                category = "Japanese",
                languageCode = "ja"
            )
        ))

        // ==========================================
        // 8. CHINESE (中文 - SIMPLIFIED)
        // ==========================================
        list.addAll(listOf(
            SnippetEntity(
                shortcut = "xiexie",
                expandedText = "非常感谢您的耐心帮助与大力支持，祝您工作顺利、万事如意！",
                label = "中文感谢",
                category = "Chinese",
                languageCode = "zh"
            ),
            SnippetEntity(
                shortcut = "nihao",
                expandedText = "您好！很高兴与您取得联系，请问有什么可以协助您的？",
                label = "问候你好",
                category = "Chinese",
                languageCode = "zh"
            ),
            SnippetEntity(
                shortcut = "zaijian",
                expandedText = "再见！祝您度过愉快充实的一天，保持联系。",
                label = "道别再见",
                category = "Chinese",
                languageCode = "zh"
            )
        ))

        // ==========================================
        // 9. BENGALI (বাংলা) & REGIONAL INDIAN LANGUAGES
        // ==========================================
        list.addAll(listOf(
            SnippetEntity(
                shortcut = "shubho",
                expandedText = "শুভকামনা এবং আন্তরিক অভিনন্দন! আপনার ভবিষ্যৎ উজ্জ্বল হোক।",
                label = "শুভকামনা (Bengali)",
                category = "Bengali",
                languageCode = "bn"
            ),
            SnippetEntity(
                shortcut = "dhonnobad",
                expandedText = "আপনাকে অনেক অনেক ধন্যবাদ আপনার অমূল্য সহযোগিতার জন্য!",
                label = "ধন্যবাদ (Bengali)",
                category = "Bengali",
                languageCode = "bn"
            ),
            SnippetEntity(
                shortcut = "kemon",
                expandedText = "কেমন আছেন আপনি? আশা করি সবকিছু ভালো চলছে।",
                label = "কেমন আছেন (Bengali)",
                category = "Bengali",
                languageCode = "bn"
            ),
            SnippetEntity(
                shortcut = "vanakkam",
                expandedText = "வணக்கம்! நீங்கள் எப்படி இருக்கிறீர்கள்? உங்கள் நாள் இனிய நாளாக அமையட்டும்.",
                label = "வணக்கம் (Tamil)",
                category = "Tamil",
                languageCode = "ta"
            ),
            SnippetEntity(
                shortcut = "namaskaram",
                expandedText = "నమస్కారం! మీరు ఎలా ఉన్నారు? మీ రోజు శుభప్రదంగా ఉండాలని కోరుకుంటున్నాను.",
                label = "నమస్కారం (Telugu)",
                category = "Telugu",
                languageCode = "te"
            ),
            SnippetEntity(
                shortcut = "satshriakal",
                expandedText = "ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ ਜੀ! ਸਭ ਸੁੱਖ ਸਾਂਦ ਹੈ? ਵਾਹਿਗੁਰੂ ਮਿਹਰ ਕਰੇ।",
                label = "ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ (Punjabi)",
                category = "Punjabi",
                languageCode = "pa"
            )
        ))

        // ==========================================
        // 10. FRENCH (FRANÇAIS) & GERMAN (DEUTSCH)
        // ==========================================
        list.addAll(listOf(
            SnippetEntity(
                shortcut = "bjr",
                expandedText = "Bonjour, j'espère que vous passez une excellente journée. N'hésitez pas à me contacter si vous avez des questions.",
                label = "Bonjour Français",
                category = "French",
                languageCode = "fr"
            ),
            SnippetEntity(
                shortcut = "mrc",
                expandedText = "Merci beaucoup pour votre aide précieuse et votre réactivité!",
                label = "Merci Français",
                category = "French",
                languageCode = "fr"
            ),
            SnippetEntity(
                shortcut = "vlg",
                expandedText = "Viele liebe Grüße und einen wunderschönen, erfolgreichen Tag noch!",
                label = "Grüße Deutsch",
                category = "German",
                languageCode = "de"
            ),
            SnippetEntity(
                shortcut = "danke",
                expandedText = "Vielen herzlichen Dank für Ihre freundliche Unterstützung und Mühe!",
                label = "Vielen Dank",
                category = "German",
                languageCode = "de"
            )
        ))

        // ==========================================
        // 11. DYNAMIC SYSTEM & PRODUCTIVITY TOKENS
        // ==========================================
        list.addAll(listOf(
            SnippetEntity(
                shortcut = "/stamp",
                expandedText = "Log recorded on {date} at {time} | Clipboard Audit: [{clipboard}]",
                label = "Date, Time & Clipboard",
                category = "Tools",
                languageCode = "any"
            ),
            SnippetEntity(
                shortcut = "/today",
                expandedText = "Today's Date: {date}",
                label = "Current Date",
                category = "Tools",
                languageCode = "any",
                triggerMode = "AUTO"
            ),
            SnippetEntity(
                shortcut = "/time",
                expandedText = "{time}",
                label = "Current Time",
                category = "Tools",
                languageCode = "any",
                triggerMode = "AUTO"
            ),
            SnippetEntity(
                shortcut = "/clip",
                expandedText = "Quoted Content: \"{clipboard}\"",
                label = "Clipboard Wrapper",
                category = "Tools",
                languageCode = "any"
            )
        ))

        return list
    }
}
