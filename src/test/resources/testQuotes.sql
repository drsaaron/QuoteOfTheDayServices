/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  scott
 * Created: Jun 8, 2023
 */

insert into SrcVal values(1, 'source 1');
insert into SrcVal values(2, 'source 2');

insert into Quote (QuoteNum, SrcCde, QuoteTxt, CanUse) values(1, 1, 'I am a quote', 'Y');
insert into Quote (QuoteNum, SrcCde, QuoteTxt, CanUse) values(2, 1, 'this is a second quote', 'Y');
insert into Quote (QuoteNum, SrcCde, QuoteTxt, CanUse) values(3, 2, 'second source, q1', 'Y');
insert into Quote (QuoteNum, SrcCde, QuoteTxt, CanUse) values(4, 2, 'second source, q2, unusable', 'N');
