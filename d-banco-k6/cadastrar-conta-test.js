import http from 'k6/http';
import {check, sleep} from 'k6';
import { Rate, Counter } from 'k6/metrics';


const dataPayload = JSON.parse(open("data/cadastrar-conta-data.json"));

function getRandon(max) {
    return Math.floor(Math.random() * max) + 1;
}

export let errorRate = new Rate('errors');

let contasNovas = new Counter('Novas contas');
let contasJaCadastradas = new Counter('Contas já cadastras');


export let options = {
    thresholds: {
        errors: ['rate<0.1'], // <10% errors
    },
};

function contaJaCadastrada(r) {
    if(r.status === 400){
        return r.status === 400 && JSON.parse(r.body).erros[0] === "Conta já cadastrada!" ;
    }
    return false;
}

export default function() {
    const url = 'http://localhost:8081/v1/contas';

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const payload = dataPayload[getRandon(10000)];

    const res = http.post(url, JSON.stringify(payload), params);

    const result = check(res, { 'Conta cadastrada com sucesso': (r) => r.status === 201 || contaJaCadastrada(r)});

    errorRate.add(!result);

    contasNovas.add(res.status === 201 ? 1 : 0);

    contasJaCadastradas.add(contaJaCadastrada(res) ? 1 : 0);
}