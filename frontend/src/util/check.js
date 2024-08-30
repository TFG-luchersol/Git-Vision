export default class Preconditions {

    static if(condition) {
        return condition === true ? Preconditions : PreconditionsNotCheck;
    }
    
    static check(condition, message="Precondition failed"){
        if(condition !== true)
            throw new Error(message);
    }

    static checkIsNumber(obj, variableName = "Object"){
        this.check(typeof obj === 'number', `${variableName} must be a number`);
    }

    static checkIsString(obj, variableName = "Object"){
        this.check(typeof obj === 'string', `${variableName} must be a string`);
    }

    static checkPositive(obj, variableName = "Object"){
        this.checkIsNumber(obj, variableName);
        this.check(obj > 0, `${variableName} must be positive`)
    }


    static checkNotEmpty(obj, variableName = "Object") {
        this.check(obj.length !== 0, `${variableName} mustn't be empty`);
    }

    static checkNotBlank(obj, variableName = "Object") {
        this.checkIsString(obj, variableName);
        this.check(obj.trim().length !== 0, `${variableName} mustn't be blank`);
    }

    static checkNotNull(obj, variableName = "Object"){
        this.check(obj === null,`${variableName} mustn't be null`)
    }

    static checkNotUndefined(obj, variableName = "Object"){
        this.check(obj !== undefined, `${variableName} can't be undefined`)
    }

}

class PreconditionsNotCheck extends Preconditions {

    static check(condition, message=""){
        
    }
}