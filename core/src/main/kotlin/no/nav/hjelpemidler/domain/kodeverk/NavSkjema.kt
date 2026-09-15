package no.nav.hjelpemidler.domain.kodeverk

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Alle Nav-skjema som inneholder `'10-07'` fra felles kodeverk.
 *
 * @see <a href="https://kodeverk.ansatt.nav.no/kodeverk/NAVSkjema">Felles kodeverk - NAVSkjema</a>
 */
@Suppress("EnumEntryName")
enum class NavSkjema(override val kode: String, override val beskrivelse: String) : Brevkode {
    NAV_10_07_01(kode = "NAV 10-07.01", beskrivelse = "Søknad om generelt vedtak på hjelpemidler"),
    NAV_10_07_02(kode = "NAV 10-07.02", beskrivelse = "Søknad om funksjonsassistanse"),
    NAV_10_07_03(kode = "NAV 10-07.03", beskrivelse = "Søknad om hjelpemidler"),
    NAV_10_07_03_J_H_09(kode = "NAV 10-07.03 J H 09", beskrivelse = "Målskjema for seteheiser og rullestolheiser i trapp"),
    NAV_10_07_03_N(kode = "NAV 10-07.03 N", beskrivelse = "Arbeidslogg for utprøving av Innowalk"),
    NAV_10_07_04(kode = "NAV 10-07.04", beskrivelse = "Fullmakt i forbindelse med søknad om tekniske hjelpemidler"),
    NAV_10_07_05(kode = "NAV 10-07.05", beskrivelse = "Bestilling av tekniske hjelpemidler"),
    NAV_10_07_06(kode = "NAV 10-07.06", beskrivelse = "Søknad om tolk til døve, døvblinde og hørselshemmede"),
    NAV_10_07_08(kode = "NAV 10-07.08", beskrivelse = "Søknad om høreapparat/tinnitusmaskerer/tilleggsutstyr"),
    NAV_10_07_09(kode = "NAV 10-07.09", beskrivelse = "Regning for lese- og sekretærhjelp for blinde og svaksynte"),
    NAV_10_07_10(kode = "NAV 10-07.10", beskrivelse = "Søknad om ortopedisk hjelpemiddel"),
    NAV_10_07_11(kode = "NAV 10-07.11", beskrivelse = "Innsending av førstegangssøknad om ortopedisk hjelpemiddel"),
    NAV_10_07_12(kode = "NAV 10-07.12", beskrivelse = "Ortopediske hjelpemidler. Ferdigattest"),
    NAV_10_07_13(kode = "NAV 10-07.13", beskrivelse = "Innsending av søknad om fornyelse av ortopedisk hjelpemiddel"),
    NAV_10_07_14(kode = "NAV 10-07.14", beskrivelse = "Søknad om briller til forebygging eller behandling av amblyopi"),
    NAV_10_07_15(kode = "NAV 10-07.15", beskrivelse = "Søknad om dekning av utgifter til irislinser"),
    NAV_10_07_16(kode = "NAV 10-07.16", beskrivelse = "Søknad om refusjon av reiseutgifter knyttet til ortopediske hjelpemidler, parykk, tilpasningskurs, folkehøyskole, grunnmønster eller brystprotese"),
    NAV_10_07_17(kode = "NAV 10-07.17", beskrivelse = "Søknad om refusjon av reiseutgifter i forbindelse med bilstønadsordningen"),
    NAV_10_07_18(kode = "NAV 10-07.18", beskrivelse = "Søknad om stønad til tilpasningskurs"),
    NAV_10_07_19(kode = "NAV 10-07.19", beskrivelse = "Søknad om refusjon av reiseutgifter - tekniske hjelpemidler"),
    NAV_10_07_20(kode = "NAV 10-07.20", beskrivelse = "Hjelp til vurdering og utprøving"),
    NAV_10_07_21(kode = "NAV 10-07.21", beskrivelse = "Bytte av hjelpemiddel"),
    NAV_10_07_22(kode = "NAV 10-07.22", beskrivelse = "Service/reparasjon av hjelpemiddel"),
    NAV_10_07_23(kode = "NAV 10-07.23", beskrivelse = "Behov for hjelpemidler knyttet til individuell plan"),
    NAV_10_07_24(kode = "NAV 10-07.24", beskrivelse = "Søknad om stønad til ekstrautgifter på folkehøyskole"),
    NAV_10_07_25(kode = "NAV 10-07.25", beskrivelse = "Søknad om tilskudd til PC eller nettbrett"),
    NAV_10_07_26(kode = "NAV 10-07.26", beskrivelse = "Søknad om tilskudd til apper og programvare"),
    NAV_10_07_27(kode = "NAV 10-07.27", beskrivelse = "Søknad om tilskudd til rimelige hjelpemidler"),
    NAV_10_07_30(kode = "NAV 10-07.30", beskrivelse = "Søknad om lese- og sekretærhjelp for blinde og svaksynte"),
    NAV_10_07_31(kode = "NAV 10-07.31", beskrivelse = "Bytte av hjelpemiddel"),
    NAV_10_07_34(kode = "NAV 10-07.34", beskrivelse = "Tilskudd ved kjøp av briller til barn"),
    NAV_10_07_36(kode = "NAV 10-07.36", beskrivelse = "Pristilbud for behandlingsbriller eller irislinser"),
    NAV_10_07_40(kode = "NAV 10-07.40", beskrivelse = "Søknad om stønad til bil og spesialutstyr"),
    NAV_10_07_41(kode = "NAV 10-07.41", beskrivelse = "Søknad om spesialutstyr til motorkjøretøy"),
    NAV_10_07_42(kode = "NAV 10-07.42", beskrivelse = "Legeerklæring for motorkjøretøy"),
    NAV_10_07_43(kode = "NAV 10-07.43", beskrivelse = "Erklæring fra ergo- eller fysioterapeut i forbindelse med søknad om motorkjøretøy og spesialutstyr / tilpasning"),
    NAV_10_07_44(kode = "NAV 10-07.44", beskrivelse = "Tilleggsskjema for stønad til kassebil ved utagerende atferd"),
    NAV_10_07_45(kode = "NAV 10-07.45", beskrivelse = "Rekvisisjon - utprøving hjelpemiddelområdet"),
    NAV_10_07_47(kode = "NAV 10-07.47", beskrivelse = "Gjeldsbrev - moderniserings- og effektiviseringstiltak selvstendig næringsdrivende"),
    NAV_10_07_48(kode = "NAV 10-07.48", beskrivelse = "Gjeldsbrev gruppe 1"),
    NAV_10_07_49(kode = "NAV 10-07.49", beskrivelse = "Gjeldsbrev gruppe 2"),
    NAV_10_07_50(kode = "NAV 10-07.50", beskrivelse = "Søknad om førerhund"),
    NAV_10_07_53(kode = "NAV 10-07.53", beskrivelse = "Søknad om dekning av ekstraordinære veterinærutgifter for førerhund eller servicehund"),
    NAV_10_07_54(kode = "NAV 10-07.54", beskrivelse = "Søknad om servicehund"),
    NAV_10_07_55(kode = "NAV 10-07.55", beskrivelse = "Søknad om stønad til grunnmønster og søm etter grunnmønster"),
    NAV_10_07_57(kode = "NAV 10-07.57", beskrivelse = "Søknad om stønad til parykk"),
    NAV_10_07_58(kode = "NAV 10-07.58", beskrivelse = "Søknad om stønad til øyeprotese eller ansiktsprotese"),
    NAV_10_07_59(kode = "NAV 10-07.59", beskrivelse = "Søknad om stønad til brystprotese eller spesialbrystholder"),
    NAV_10_07_60(kode = "NAV 10-07.60", beskrivelse = "Søknad om stønad til alminnelig fottøy ved ulik fotstørrelse"),
    NAV_10_07_61(kode = "NAV 10-07.61", beskrivelse = "Søknad om refusjon av betalt egenandel for fottøy, fotseng og høreapparat ved yrkesskade"),
    NAV_10_07_63(kode = "NAV 10-07.63", beskrivelse = "Innlevering av tekniske hjelpemidler"),
    NAV_10_07_64(kode = "NAV 10-07.64", beskrivelse = "T10 Tilleggsskjema for stasjonær personløfter"),
    NAV_10_07_65(kode = "NAV 10-07.65", beskrivelse = "T04 Tilleggsskjema for manuell rullestol"),
    NAV_10_07_66(kode = "NAV 10-07.66", beskrivelse = "T05 Tilleggsskjema for elektrisk rullestol"),
    NAV_10_07_67(kode = "NAV 10-07.67", beskrivelse = "T06 Tilleggsskjema for stol med oppreisingsfunksjon"),
    NAV_10_07_68(kode = "NAV 10-07.68", beskrivelse = "T02 Tilleggsskjema for omgivelseskontroll"),
    NAV_10_07_69(kode = "NAV 10-07.69", beskrivelse = "T09 Tilleggsskjema for tilrettelegging av adkomst til og i bolig"),
    NAV_10_07_70(kode = "NAV 10-07.70", beskrivelse = "T01 Tilleggsskjema for hjelpemidler på bad"),
    NAV_10_07_71(kode = "NAV 10-07.71", beskrivelse = "T03 Tilleggsskjema for hev- og senkbare kjøkkenløsninger"),
    NAV_10_07_72(kode = "NAV 10-07.72", beskrivelse = "T08 Tilleggsskjema for kommunikasjonshjelpemiddel"),
    NAV_10_07_73(kode = "NAV 10-07.73", beskrivelse = "T12 Tilleggsskjema for hjelpemidler og tilrettelegging i arbeidslivet"),
    NAV_10_07_74(kode = "NAV 10-07.74", beskrivelse = "T11 Tilleggsskjema for hjelpemidler til trening, stimulering og aktivisering"),
    NAV_10_07_75(kode = "NAV 10-07.75", beskrivelse = "Arbeidslogg for utprøving av Innowalk som grunnlag for helhetsvurdering og vedlegg til søknad"),
    NAV_10_07_76(kode = "NAV 10-07.76", beskrivelse = "T13 Tilleggsskjema for kognitivt hjelpemiddel"),
    NAV_10_07_77(kode = "NAV 10-07.77", beskrivelse = "T15 Tilleggsskjema for synshjelpemiddel"),
    NAV_10_07_78(kode = "NAV 10-07.78", beskrivelse = "T07 Tilleggsskjema for hørselshjelpemiddel"),
    NAV_10_07_79(kode = "NAV 10-07.79", beskrivelse = "Søknad om briller til behandling og/eller forebygging av amblyopi"),
    NAV_10_07_80(kode = "NAV 10-07.80", beskrivelse = "Bekreftelse på utlån og tildeling av høreapparat / tinnitusmaskerer / tilleggsutstyr"),
    NAV_10_07_86(kode = "NAV 10-07.86", beskrivelse = "Søknad om hjelpemiddel til kognisjon, kommunikasjon og lese- og skrivevansker"),
    NAV_10_07_87(kode = "NAV 10-07.87", beskrivelse = "Søknad om synshjelpemidler"),
    NAVe_10_07_01(kode = "NAVe 10-07.01", beskrivelse = "Ettersendelse til Søknad om generelt vedtak på hjelpemidler"),
    NAVe_10_07_02(kode = "NAVe 10-07.02", beskrivelse = "Ettersendelse til Søknad om funksjonsassistanse"),
    NAVe_10_07_03(kode = "NAVe 10-07.03", beskrivelse = "Ettersendelse til Søknad om hjelpemidler"),
    NAVe_10_07_03_J_H_09(kode = "NAVe 10-07.03 J H 09", beskrivelse = "Ettersendelse til Målskjema for seteheiser og rullestolheiser i trapp"),
    NAVe_10_07_03_N(kode = "NAVe 10-07.03 N", beskrivelse = "Ettersendelse til Arbeidslogg for utprøving av Innowalk"),
    NAVe_10_07_04(kode = "NAVe 10-07.04", beskrivelse = "Ettersendelse til Fullmakt i forbindelse med søknad om tekniske hjelpemidler"),
    NAVe_10_07_05(kode = "NAVe 10-07.05", beskrivelse = "Ettersendelse til Bestilling av tekniske hjelpemidler"),
    NAVe_10_07_06(kode = "NAVe 10-07.06", beskrivelse = "Ettersendelse til Søknad om tolk til døve, døvblinde og hørselshemmede"),
    NAVe_10_07_08(kode = "NAVe 10-07.08", beskrivelse = "Ettersendelse til Søknad om høreapparat/tinnitusmaskerer/tilleggsutstyr"),
    NAVe_10_07_09(kode = "NAVe 10-07.09", beskrivelse = "Ettersendelse til Regning for lese- og sekretærhjelp for blinde og svaksynte"),
    NAVe_10_07_10(kode = "NAVe 10-07.10", beskrivelse = "Ettersendelse til Søknad om ortopedisk hjelpemiddel"),
    NAVe_10_07_11(kode = "NAVe 10-07.11", beskrivelse = "Ettersendelse til Innsending av Søknad om ortopedisk hjelpemiddel"),
    NAVe_10_07_12(kode = "NAVe 10-07.12", beskrivelse = "Ettersendelse til Ortopediske hjelpemidler. Ferdigattest"),
    NAVe_10_07_13(kode = "NAVe 10-07.13", beskrivelse = "Ettersendelse til Innsending av søknad om fornyelse av ortopedisk hjelpemiddel"),
    NAVe_10_07_14(kode = "NAVe 10-07.14", beskrivelse = "Ettersendelse til Søknad om briller til forebygging eller behandling av amblyopi"),
    NAVe_10_07_15(kode = "NAVe 10-07.15", beskrivelse = "Ettersendelse til Søknad om dekning av utgifter til irislinse"),
    NAVe_10_07_16(kode = "NAVe 10-07.16", beskrivelse = "Ettersendelse til Søknad om refusjon av reiseutgifter knyttet til ortopediske hjelpemidler, parykk, tilpasningskurs, folkehøyskole, grunnmønster eller brystprotese"),
    NAVe_10_07_17(kode = "NAVe 10-07.17", beskrivelse = "Ettersendelse til Søknad om refusjon av reiseutgifter i forbindelse med bilstønadsordningen"),
    NAVe_10_07_18(kode = "NAVe 10-07.18", beskrivelse = "Ettersendelse til Søknad om stønad til tilpasningskurs"),
    NAVe_10_07_19(kode = "NAVe 10-07.19", beskrivelse = "Ettersendelse til Søknad om refusjon av reiseutgifter - tekniske hjelpemidler"),
    NAVe_10_07_20(kode = "NAVe 10-07.20", beskrivelse = "Ettersendelse til Hjelp til vurdering og utprøving"),
    NAVe_10_07_21(kode = "NAVe 10-07.21", beskrivelse = "Ettersendelse til Bytte av hjelpemiddel"),
    NAVe_10_07_22(kode = "NAVe 10-07.22", beskrivelse = "Ettersendelse til Service/reparasjon av hjelpemiddel"),
    NAVe_10_07_23(kode = "NAVe 10-07.23", beskrivelse = "Ettersendelse til Behov for hjelpemidler knyttet til individuell plan"),
    NAVe_10_07_24(kode = "NAVe 10-07.24", beskrivelse = "Ettersendelse til Søknad om stønad til ekstrautgifter på folkehøyskole"),
    NAVe_10_07_25(kode = "NAVe 10-07.25", beskrivelse = "Ettersendelse til Søknad om tilskudd til PC eller nettbrett"),
    NAVe_10_07_26(kode = "NAVe 10-07.26", beskrivelse = "Ettersendelse til Søknad om tilskudd til apper og programvare"),
    NAVe_10_07_27(kode = "NAVe 10-07.27", beskrivelse = "Ettersendelse til Søknad om tilskudd til rimelige hjelpemidler"),
    NAVe_10_07_30(kode = "NAVe 10-07.30", beskrivelse = "Ettersendelse til Søknad om lese- og sekretærhjelp for blinde og svaksynte"),
    NAVe_10_07_31(kode = "NAVe 10-07.31", beskrivelse = "Ettersendelse til Bytte av hjelpemiddel"),
    NAVe_10_07_34(kode = "NAVe 10-07.34", beskrivelse = "Ettersendelse til refusjon ved kjøp av briller til barn"),
    NAVe_10_07_36(kode = "NAVe 10-07.36", beskrivelse = "Ettersendelse til Pristilbud for behandlingsbriller eller irislinser"),
    NAVe_10_07_40(kode = "NAVe 10-07.40", beskrivelse = "Ettersendelse til Søknad om stønad til anskaffelse av motorkjøretøy og / eller spesialutstyr og tilpassing til bil"),
    NAVe_10_07_42(kode = "NAVe 10-07.42", beskrivelse = "Ettersendelse til Legeerklæring for motorkjøretøy"),
    NAVe_10_07_43(kode = "NAVe 10-07.43", beskrivelse = "Ettersendelse til Erklæring fra ergo- eller fysioterapeut i forbindelse med søknad om motorkjøretøy og spesialutstyr / tilpasning"),
    NAVe_10_07_44(kode = "NAVe 10-07.44", beskrivelse = "Ettersendelse til Tilleggsskjema for utagerende adferd i bil"),
    NAVe_10_07_45(kode = "NAVe 10-07.45", beskrivelse = "Ettersendelse til Rekvisisjon - utprøving hjelpemiddelområdet"),
    NAVe_10_07_47(kode = "NAVe 10-07.47", beskrivelse = "Ettersendelse til Gjeldsbrev - moderniserings- og effektiviseringstiltak selvstendig næringsdrivende"),
    NAVe_10_07_48(kode = "NAVe 10-07.48", beskrivelse = "Ettersendelse til Gjeldsbrev gruppe 1"),
    NAVe_10_07_49(kode = "NAVe 10-07.49", beskrivelse = "Ettersendelse til Gjeldsbrev gruppe 2"),
    NAVe_10_07_50(kode = "NAVe 10-07.50", beskrivelse = "Ettersendelse til Søknad om førerhund"),
    NAVe_10_07_53(kode = "NAVe 10-07.53", beskrivelse = "Ettersendelse til Søknad om dekning av ekstraordinære veterinærutgifter for førerhund eller servicehund"),
    NAVe_10_07_54(kode = "NAVe 10-07.54", beskrivelse = "Ettersendelse til Søknad om servicehund"),
    NAVe_10_07_55(kode = "NAVe 10-07.55", beskrivelse = "Ettersendelse til Søknad om stønad til grunnmønster og søm etter grunnmønster"),
    NAVe_10_07_57(kode = "NAVe 10-07.57", beskrivelse = "Ettersendelse til Søknad om stønad til parykk"),
    NAVe_10_07_58(kode = "NAVe 10-07.58", beskrivelse = "Ettersendelse til Søknad om stønad til øyeprotese eller ansiktsprotese"),
    NAVe_10_07_59(kode = "NAVe 10-07.59", beskrivelse = "Ettersendelse til Søknad om stønad til brystprotese eller spesialbrystholder"),
    NAVe_10_07_60(kode = "NAVe 10-07.60", beskrivelse = "Ettersendelse til Søknad om stønad til alminnelig fottøy ved ulik fotstørrelse"),
    NAVe_10_07_61(kode = "NAVe 10-07.61", beskrivelse = "Ettersendelse til Søknad om refusjon av betalt egenandel for fottøy, fotseng og høreapparat ved yrkesskade"),
    NAVe_10_07_63(kode = "NAVe 10-07.63", beskrivelse = "Ettersendelse til Innlevering av tekniske hjelpemidler"),
    NAVe_10_07_64(kode = "NAVe 10-07.64", beskrivelse = "Ettersendelse til T10 Tilleggsskjema for stasjonær personløfter"),
    NAVe_10_07_65(kode = "NAVe 10-07.65", beskrivelse = "Ettersendelse til T04 Tilleggsskjema for manuell rullestol"),
    NAVe_10_07_66(kode = "NAVe 10-07.66", beskrivelse = "Ettersendelse til T05 Tilleggsskjema for elektrisk rullestol"),
    NAVe_10_07_67(kode = "NAVe 10-07.67", beskrivelse = "Ettersendelse til T06 Tilleggsskjema for stol med oppreisingsfunksjon"),
    NAVe_10_07_68(kode = "NAVe 10-07.68", beskrivelse = "Ettersendelse til T02 Tilleggsskjema for omgivelseskontroll"),
    NAVe_10_07_69(kode = "NAVe 10-07.69", beskrivelse = "Ettersendelse til T09 Tilleggsskjema for tilrettelegging av adkomst til og i bolig"),
    NAVe_10_07_70(kode = "NAVe 10-07.70", beskrivelse = "Ettersendelse til T01 Tilleggsskjema for hjelpemidler på bad"),
    NAVe_10_07_71(kode = "NAVe 10-07.71", beskrivelse = "Ettersendelse til T03 Tilleggsskjema for hev- og senkbare kjøkkenløsninger"),
    NAVe_10_07_72(kode = "NAVe 10-07.72", beskrivelse = "Ettersendelse til T08 Tilleggsskjema for kommunikasjonshjelpemiddel"),
    NAVe_10_07_73(kode = "NAVe 10-07.73", beskrivelse = "Ettersendelse til T12 Tilleggsskjema for hjelpemidler og tilrettelegging i arbeidslivet"),
    NAVe_10_07_74(kode = "NAVe 10-07.74", beskrivelse = "Ettersendelse til T11 Tilleggsskjema for hjelpemidler til trening, stimulering og aktivisering"),
    NAVe_10_07_75(kode = "NAVe 10-07.75", beskrivelse = "Ettersendelse til Arbeidslogg for utprøving av Innowalk som grunnlag for helhetsvurdering og vedlegg til søknad"),
    NAVe_10_07_76(kode = "NAVe 10-07.76", beskrivelse = "Ettersendelse til T13 Tilleggsskjema for kognitivt hjelpemiddel"),
    NAVe_10_07_77(kode = "NAVe 10-07.77", beskrivelse = "Ettersendelse til T15 Tilleggsskjema for synshjelpemiddel"),
    NAVe_10_07_78(kode = "NAVe 10-07.78", beskrivelse = "Ettersendelse til T07 Tilleggsskjema for hørselshjelpemiddel"),
    NAVe_10_07_79(kode = "NAVe 10-07.79", beskrivelse = "Ettersendelse til Søknad om briller til behandling og/eller forebygging av amblyopi"),
    NAVe_10_07_80(kode = "NAVe 10-07.80", beskrivelse = "Ettersendelse til Bekreftelse på utlån og tildeling av høreapparat / tinnitusmaskerer / tilleggsutstyr"),
    NAVe_10_07_86(kode = "NAVe 10-07.86", beskrivelse = "Ettersendelse til Søknad om hjelpemiddel til kognisjon, kommunikasjon og lese- og skrivevansker"),
    NAVe_10_07_87(kode = "NAVe 10-07.87", beskrivelse = "Ettersendelse til Søknad om synshjelpemidler"),
    NAVp_10_07_03(kode = "NAVp 10-07.03", beskrivelse = "Søknad om hjelpemidler"),
    NAVp_10_07_04(kode = "NAVp 10-07.04", beskrivelse = "Fullmakt i forbindelse med søknad om tekniske hjelpemidler"),
    NAVp_10_07_08(kode = "NAVp 10-07.08", beskrivelse = "Søknad om høreapparat / tinnitusmaskerer / tilleggsutstyr"),
    NAVp_10_07_09(kode = "NAVp 10-07.09", beskrivelse = "Regning for lese- og sekretærhjelp for blinde og svaksynte"),
    NAVp_10_07_20(kode = "NAVp 10-07.20", beskrivelse = "Hjelp til vurdering og utprøving av hjelpemidler på arbeidsplassen"),
    NAVp_10_07_24(kode = "NAVp 10-07.24", beskrivelse = "Søknad om stønad til ekstrautgifter på folkehøyskole"),
    NAVp_10_07_31(kode = "NAVp 10-07.31", beskrivelse = "Bytte av hjelpemiddel"),
    NAVp_10_07_63(kode = "NAVp 10-07.63", beskrivelse = "Innlevering av tekniske hjelpemidler"),
    NAVp_10_07_64(kode = "NAVp 10-07.64", beskrivelse = "T10 Tilleggsskjema for stasjonær personløfter"),
    NAVp_10_07_65(kode = "NAVp 10-07.65", beskrivelse = "T04 Tilleggsskjema for manuell rullestol"),
    NAVp_10_07_66(kode = "NAVp 10-07.66", beskrivelse = "T05 Tilleggsskjema for elektrisk rullestol"),
    NAVp_10_07_67(kode = "NAVp 10-07.67", beskrivelse = "T06 Tilleggsskjema for stol med oppreisingsfunksjon"),
    NAVp_10_07_68(kode = "NAVp 10-07.68", beskrivelse = "T02 Tilleggsskjema for omgivelseskontroll"),
    NAVp_10_07_69(kode = "NAVp 10-07.69", beskrivelse = "T09 Tilleggsskjema for tilrettelegging av adkomst til og i bolig"),
    NAVp_10_07_70(kode = "NAVp 10-07.70", beskrivelse = "T01 Tilleggsskjema for hjelpemidler på bad"),
    NAVp_10_07_71(kode = "NAVp 10-07.71", beskrivelse = "T03 Tilleggsskjema for hev- og senkbare kjøkkenløsninger"),
    NAVp_10_07_72(kode = "NAVp 10-07.72", beskrivelse = "T08 Tilleggsskjema for kommunikasjonshjelpemiddel"),
    NAVp_10_07_73(kode = "NAVp 10-07.73", beskrivelse = "T12 Tilleggsskjema for hjelpemidler og tilrettelegging i arbeidslivet"),
    NAVp_10_07_74(kode = "NAVp 10-07.74", beskrivelse = "T11 Tilleggsskjema for hjelpemidler til trening, stimulering og aktivisering"),
    NAVp_10_07_75(kode = "NAVp 10-07.75", beskrivelse = "Arbeidslogg for utprøving av Innowalk som grunnlag for helhetsvurdering og vedlegg til søknad"),
    NAVp_10_07_76(kode = "NAVp 10-07.76", beskrivelse = "T13 Tilleggsskjema for kognitivt hjelpemiddel"),
    NAVp_10_07_78(kode = "NAVp 10-07.78", beskrivelse = "T07 Tilleggsskjema for hørselshjelpemiddel");

    val isEttersendelse: Boolean get() = kode.startsWith(PREFIX_ETTERSENDELSE)
    val isStatisk: Boolean get() = kode.startsWith(PREFIX_STATISK)

    companion object {
        const val PREFIX = "NAV"
        const val PREFIX_ETTERSENDELSE = "NAVe"

        /**
         * Gammelt PDF-skjema.
         */
        const val PREFIX_STATISK = "NAVp"

        private val navSkjemaByKode: Map<String, NavSkjema> = entries.associateBy { it.kode }
        operator fun get(kode: String): NavSkjema? = navSkjemaByKode[kode]

        fun isNavSkjema(kode: String): Boolean = kode.startsWith(PREFIX)
                || kode.startsWith(PREFIX_ETTERSENDELSE)
                || kode.startsWith(PREFIX_STATISK)
    }
}

@JvmInline
value class UkjentNavSkjema(override val kode: String) : Brevkode {
    override fun toString() = kode
}

@OptIn(ExperimentalContracts::class)
val Brevkode.isNavSkjema: Boolean
    get() {
        contract {
            returns(true) implies (this@isNavSkjema is NavSkjema)
        }
        return this is NavSkjema
    }

@OptIn(ExperimentalContracts::class)
val Brevkode.isUkjentNavSkjema: Boolean
    get() {
        contract {
            returns(true) implies (this@isUkjentNavSkjema is UkjentNavSkjema)
        }
        return this is UkjentNavSkjema
    }
