/**
 * DOMAIN (Cekirdek / Is Kurallari) katmani - Clean Architecture'in en ic halkasi.
 * <p>
 * Bu pakette ve alt paketlerinde (model, exception) yer alan tum siniflar
 * BILINCLI OLARAK hicbir framework'e (Spring, JPA, vb.) bagimli degildir -
 * saf Java'dir. Bunun nedeni Clean Architecture'in "Bagimlilik Kurali"
 * (Dependency Rule): dis katmanlar (infrastructure, presentation) ic
 * katmanlara (domain) bagimli olabilir, ama domain hicbir disa bagimli
 * OLAMAZ. Bu sayede is kurallari (Appointment'in durum gecisleri, doktorun
 * aylik ek odeme hesabi, randevu cakisma kontrolu gibi) veritabanindan veya
 * web framework'unden tamamen bagimsiz, kolay test edilebilir ve her OOP
 * dilinde tasinabilir sekilde kalir.
 * <p>
 * Alt paketler:
 * <ul>
 *   <li>model.person   - Person/Employee hiyerarsisi (Patient, Doctor, Staff, FacilityManager)</li>
 *   <li>model.facility - HealthcareFacility hiyerarsisi (Hospital, Clinic), Department, HealthcareOrganization</li>
 *   <li>model.service  - MedicalService (sunulan saglik hizmetleri)</li>
 *   <li>model.appointment - Appointment ve AppointmentStatus</li>
 *   <li>model.common   - Address, TimeSlot gibi degismez deger nesneleri</li>
 *   <li>model.notification - Notifiable arayuzu (coklu-arayuz polimorfizm ornegi)</li>
 *   <li>exception      - is kurali ihlallerini temsil eden istisnalar</li>
 * </ul>
 */
package com.hastanesistemi.randevu.domain;
